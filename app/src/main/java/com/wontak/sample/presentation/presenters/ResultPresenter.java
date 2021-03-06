package com.wontak.sample.presentation.presenters;

import com.wontak.sample.domain.interactors.GetUserRepositoriesUseCase;
import com.wontak.sample.domain.interactors.GetUserUseCase;
import com.wontak.sample.domain.models.Repository;
import com.wontak.sample.domain.models.User;
import com.wontak.sample.presentation.ui.listeners.ResultView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public class ResultPresenter {

    private GetUserUseCase getUserUseCase;
    private GetUserRepositoriesUseCase getUserRepositoriesUseCase;

    private ResultView.View view;

    @Inject
    public ResultPresenter(GetUserUseCase getUserUseCase, GetUserRepositoriesUseCase getUserRepositoriesUseCase) {
        this.getUserUseCase = getUserUseCase;
        this.getUserRepositoriesUseCase = getUserRepositoriesUseCase;
    }

    public void destroy() {
        getUserUseCase.dispose();
        getUserRepositoriesUseCase.dispose();
    }

    public void setView(ResultView.View view) {
        this.view = view;
    }

    public void getUser(String username) {
        getUserUseCase.execute(new GetUserDisposableObserver(), username);
    }

    public void getUserRepositories(String username) {
        getUserRepositoriesUseCase.execute(new GetUserRepositoriesDisposableObserver(), username);
    }

    private void showUserDetailsInView(User user) {
        view.showUser(user);
    }

    private void showRepositoriesInView(List<Repository> repositories) {
        view.showRepositories(repositories);
    }

    private void hideViewLoading() {
        view.hideLoading();
    }

    private void showErrorMessage(String message) {
        view.showError(message);
    }

    private final class GetUserDisposableObserver extends DisposableObserver<User> {

        @Override
        public void onComplete() {

        }

        @Override
        public void onError(Throwable e) {
            hideViewLoading();
            showErrorMessage(e.getMessage());
        }

        @Override
        public void onNext(User user) {
            showUserDetailsInView(user);
        }
    }

    private final class GetUserRepositoriesDisposableObserver extends DisposableObserver<List<Repository>> {

        @Override
        public void onComplete() {
            hideViewLoading();
        }

        @Override
        public void onError(Throwable e) {
            hideViewLoading();
            showErrorMessage(e.getMessage());
        }

        @Override
        public void onNext(List<Repository> repositories) {
            showRepositoriesInView(repositories);
        }
    }
}
