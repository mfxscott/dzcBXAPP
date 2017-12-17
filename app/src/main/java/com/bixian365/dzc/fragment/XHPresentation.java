package com.bixian365.dzc.fragment;

import android.annotation.SuppressLint;
import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;

import com.bixian365.dzc.R;

@SuppressLint("NewApi")
public class XHPresentation extends Presentation 
{

	public XHPresentation(Context outerContext, Display display) 
	{
		super(outerContext, display);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.screen_second);

	}

}
