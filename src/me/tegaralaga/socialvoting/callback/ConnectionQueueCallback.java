package me.tegaralaga.socialvoting.callback;

public interface ConnectionQueueCallback {
	public void response(int returnCode, byte[] response);
	public void response(int returnCode, String response);
}
