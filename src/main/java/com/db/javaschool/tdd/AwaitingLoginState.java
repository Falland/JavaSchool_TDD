package com.db.javaschool.tdd;

/**
 * User: Yury
 */
public class AwaitingLoginState implements ILoginState {
    @Override
    public ILoginState login(IAccount account, String password) {
        if (account.isRevoked()) {
            throw new IllegalStateException("Account is revoked");
        }
        if (account.passwordMatches(password)) {
            account.setLoggedIn();
            return this;
        }
        return new FirstAttemptFailedState();
    }
}
