package com.example.simpletext;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

public class EditTextActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_text_activity);
		
		ToggleButton boldToggle = (ToggleButton) findViewById(R.id.BoldButton);
		ToggleButton italicsToggle = (ToggleButton)findViewById(R.id.ItalicsButton);
		ToggleButton underlineToggle = (ToggleButton)findViewById(R.id.UnderlineButton);
		
		View coolButton = findViewById(R.id.CoolButton);
		View cryButton =findViewById(R.id.CryButton);
		
		Button clearButton = (Button)findViewById(R.id.ClearButton);
		
		SimpleEditText seText = (SimpleEditText)findViewById(R.id.SeText);
		seText.setImageGetter(new Html.ImageGetter() {
			
			@Override
			public Drawable getDrawable(String source) {
				// TODO Auto-generated method stub
				Drawable drawable =null;
				
				try {
					if (source.equals("smiley_cool.gif")) {
						drawable = getResources().getDrawable(R.drawable.smiley_cool);
					}else if (source.equals("smiley_cry.gif")) {
						drawable = getResources().getDrawable(R.drawable.smiley_cry);
					}else {
						drawable =null;
					}
					
					//important 
					if (drawable != null) {
						drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("SimpleEditText", "Failed to load inline image");
				}
				return drawable;
			}
		});
		
		seText.setSingleLine(false);
		seText.setMinLines(15);
		seText.setBoldToggleButton(boldToggle);
		seText.setItalicsToggleButton(italicsToggle);
		seText.setUnderlineToggleButton(underlineToggle);
		
		seText.setImageInsertButton(coolButton, "smiley_cool.gif");
		seText.setImageInsertButton(cryButton, "smiley_cry.gif");
		
		seText.setClearButton(clearButton);
	}

	public EditTextActivity() {
		// TODO Auto-generated constructor stub
	}
	
}
