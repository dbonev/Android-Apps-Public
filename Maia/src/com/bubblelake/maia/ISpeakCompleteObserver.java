package com.bubblelake.maia;

public interface ISpeakCompleteObserver {
	void speakCompleted(Greeting g);
	void onPause(Greeting g);
	void onResume(Greeting g);
}