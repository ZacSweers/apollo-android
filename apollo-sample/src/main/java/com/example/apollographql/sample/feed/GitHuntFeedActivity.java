package com.example.apollographql.sample.feed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.cache.normalized.CacheControl;
import com.apollographql.apollo.exception.ApolloException;
import com.example.FeedQuery;
import com.example.apollographql.sample.R;
import com.example.apollographql.sample.GitHuntApplication;
import com.example.apollographql.sample.detail.GitHuntEntryDetailActivity;
import com.example.type.FeedType;

import javax.annotation.Nonnull;

public class GitHuntFeedActivity extends AppCompatActivity implements GitHuntNavigator {

  private static final String TAG = GitHuntFeedActivity.class.getSimpleName();

  private GitHuntApplication application;

  private ViewGroup content;
  private ProgressBar progressBar;
  private GitHuntFeedRecyclerViewAdapter feedAdapter;
  private static final int FEED_SIZE = 20;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_githunt_feed);
    application = (GitHuntApplication) getApplication();

    content = (ViewGroup) findViewById(R.id.content_holder);
    progressBar = (ProgressBar) findViewById(R.id.loading_bar);

    RecyclerView feedRecyclerView = (RecyclerView) findViewById(R.id.rv_feed_list);
    feedAdapter = new GitHuntFeedRecyclerViewAdapter(this);
    feedRecyclerView.setAdapter(feedAdapter);
    feedRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    fetchFeed();
  }

  private ApolloCall.Callback<FeedQuery.Data> dataCallback = new ApolloCall.Callback<FeedQuery.Data>() {
    @Override public void onResponse(@Nonnull final Response<FeedQuery.Data> dataResponse) {
      GitHuntFeedActivity.this.runOnUiThread(new Runnable() {
        @Override public void run() {
          feedAdapter.setFeed(dataResponse.data().feedEntries());
          progressBar.setVisibility(View.GONE);
          content.setVisibility(View.VISIBLE);
        }
      });
    }

    @Override public void onFailure(@Nonnull ApolloException e) {
      Log.e(TAG, e.getMessage(), e);
    }
  };

  private void fetchFeed() {
    ApolloCall<FeedQuery.Data> githuntFeedCall = application.apolloClient().newCall(FeedQuery.builder()
        .limit(FEED_SIZE)
        .type(FeedType.HOT)
        .build())
        .cacheControl(CacheControl.NETWORK_FIRST);
    githuntFeedCall.enqueue(dataCallback);
  }

  @Override public void startGitHuntActivity(String repoFullName) {
    final Intent intent = GitHuntEntryDetailActivity.newIntent(this, repoFullName);
    startActivity(intent);
  }
}

