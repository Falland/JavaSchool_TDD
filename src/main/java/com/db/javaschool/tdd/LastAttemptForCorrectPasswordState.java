package com.db.javaschool.tdd;

/**
 * User: Yury
 */
public class LastAttemptForCorrectPasswordState implements ILoginState {

    @Override
    public ILoginState login(IAccount account, String password) {
        if (account.passwordMatches(password)) {
            account.setLoggedIn();
            return new AwaitingLoginState();
        }
        account.setRevoked();
        return new AwaitingLoginState();
    }
}
