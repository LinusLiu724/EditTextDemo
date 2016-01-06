package com.example.simpletext;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

public class SimpleEditText extends EditText {

	
	// Style constants
	private static final int STYLE_BOLD = 0;
	private static final int STYLE_ITALIC = 1;
	private static final int STYLE_UNDERLINED = 2;
	
	// Optional styling button references
	private ToggleButton boldToggle;
	private ToggleButton italicsToggle;
	private ToggleButton underlineToggle;
	
	
	private Html.ImageGetter imageGetter;
	
	public SimpleEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initialize();
	}

	public SimpleEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initialize();
	}

	public SimpleEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initialize();
	}
	
	private void initialize(){
		//Add a default imageGetter
		imageGetter  = new  Html.ImageGetter() {
			
			@Override
			public Drawable getDrawable(String source) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		//Add TextWatcher what reacts to text changes and applies the selected styles
		this.addTextChangedListener(new SETextWatcher());
		
	}
	/**
	 * When the user selects a section of the text, this method is used to
	 * toggle the defined style on it. If the selected text already has the
	 * style applied, we remove it, otherwise we apply it.
	 * 
	 * @param style
	 *            The styles that should be toggled on the selected text.
	 */
	
	private void toggleStyle(int style) {
		//gets the current cursor position, or the starting position of the selection
		int selectionStart = this.getSelectionStart();
		//Gets the current cursor position ,or the end position of selection 
		//Note:the end can be smaller than the start 
		int selectionEnd = this.getSelectionEnd();
		//Reverse if the case is what's noted above
		if (selectionStart > selectionEnd) {
			int  temp = selectionEnd;
			selectionEnd = selectionStart;
			selectionStart = temp;
			
		}
		
		// The selectionEnd is only greater then the selectionStart position
		// when the user selected a section of the text. Otherwise, the 2
		// variables
		// should be equal (the cursor position).
		if (selectionEnd > selectionStart) {
			Spannable str = this.getText();
			boolean exists = false;
			StyleSpan[] styleSpans;
			
			switch (style) {
			case STYLE_BOLD:
				styleSpans = str.getSpans(selectionStart, selectionEnd, StyleSpan.class);

				// If the selected text-part already has BOLD style on it, then
				// we need to disable it
				for (int i = 0; i < styleSpans.length; i++) {
					if(styleSpans[i].getStyle() == android.graphics.Typeface.BOLD){
						str.removeSpan(styleSpans[i]);
						exists = true;
					}
				}
				// Else we set BOLD style on it
				if (!exists) {
					str.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), selectionStart, selectionEnd,
							Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				}
				
				this.setSelection(selectionStart, selectionEnd);
				break;
			case STYLE_ITALIC:
				styleSpans = str.getSpans(selectionStart, selectionEnd, StyleSpan.class);

				// If the selected text-part already has ITALIC style on it,
				// then we need to disable it
				for (int i = 0; i < styleSpans.length; i++) {
					if (styleSpans[i].getStyle() == android.graphics.Typeface.ITALIC) {
						str.removeSpan(styleSpans[i]);
						exists = true;
					}
				}

				// Else we set ITALIC style on it
				if (!exists) {
					str.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), selectionStart, selectionEnd,
							Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				}

				this.setSelection(selectionStart, selectionEnd);
				break;
			case STYLE_UNDERLINED:
				UnderlineSpan[] underSpan = str.getSpans(selectionStart, selectionEnd, UnderlineSpan.class);

				// If the selected text-part already has UNDERLINE style on it,
				// then we need to disable it
				for (int i = 0; i < underSpan.length; i++) {
					str.removeSpan(underSpan[i]);
					exists = true;
				}

				// Else we set UNDERLINE style on it
				if (!exists) {
					str.setSpan(new UnderlineSpan(), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				}

				this.setSelection(selectionStart, selectionEnd);
				break;
			}
			
		}
	}
	
	/**
	 * This method makes sure that the optional style toggle buttons update
	 * their state correctly when the user moves the cursor around the EditText,
	 * or when the user selects sections of the text.
	 */
	
	@Override
	public void onSelectionChanged(int selStart, int selEnd){
		boolean boldExists = false;
		boolean italicsExists = false;
		boolean underlinedExists = false;
		
		// If the user only placed the cursor around 
		if(selStart > 0 && selStart == selEnd){
			CharacterStyle[] styleSpans = this.getText().getSpans(selStart - 1, selStart, CharacterStyle.class);
			
			for(int i = 0; i < styleSpans.length; i++){
				if (styleSpans[i] instanceof StyleSpan) {
					if(((StyleSpan)styleSpans[i]).getStyle() == android.graphics.Typeface.BOLD){
						boldExists = true;
					}else if (((StyleSpan)styleSpans[i]).getStyle() == android.graphics.Typeface.ITALIC) {
						italicsExists = true;
					}else if (((StyleSpan)styleSpans[i]).getStyle() == android.graphics.Typeface.BOLD_ITALIC) {
						italicsExists = true;
						boldExists = true;
					}
				}else if (styleSpans[i] instanceof UnderlineSpan) {
					underlinedExists =true;
				}
			}
		}
		
		//Else if the user selected multiple characters
		else {
			CharacterStyle[] styleSpans = this.getText().getSpans(selStart, selEnd, CharacterStyle.class);
			
			for(int i = 0; i < styleSpans.length; i++){
				if(styleSpans[i] instanceof StyleSpan){
					if (((StyleSpan)styleSpans[i]).getStyle() == android.graphics.Typeface.BOLD) {
						if (this.getText().getSpanStart(styleSpans[i]) <= selStart
								&& this.getText().getSpanEnd(styleSpans[i]) >= selEnd) {
							boldExists = true;
						}
					}else if (((StyleSpan)styleSpans[i]).getStyle() == android.graphics.Typeface.ITALIC) {
						if (this.getText().getSpanStart(styleSpans[i]) <= selStart
								&& this.getText().getSpanEnd(styleSpans[i]) >= selEnd) {
							italicsExists = true;
						}
					}else if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.BOLD_ITALIC) {
						if (this.getText().getSpanStart(styleSpans[i]) <= selStart
								&& this.getText().getSpanEnd(styleSpans[i]) >= selEnd) {
							italicsExists = true;
							boldExists = true;
						}
					}
				} else if (styleSpans[i] instanceof UnderlineSpan) {
					if (this.getText().getSpanStart(styleSpans[i]) <= selStart
							&& this.getText().getSpanEnd(styleSpans[i]) >= selEnd) {
						underlinedExists = true;
					}
				}
			}
		}

		// Display the format settings
		if (boldToggle != null) {
			if (boldExists)
				boldToggle.setChecked(true);
			else
				boldToggle.setChecked(false);
		}
		
		if (italicsToggle != null) {
			if (italicsExists)
				italicsToggle.setChecked(true);
			else
				italicsToggle.setChecked(false);
		}

		if (underlineToggle != null) {
			if (underlinedExists)
				underlineToggle.setChecked(true);
			else
				underlineToggle.setChecked(false);
		}
	}
	
	// Get and set Spanned, styled text
	public Spanned getSpannedText() {
		return this.getText();
	}
	public void setSpannedText(Spanned text) {
		this.setText(text);
	}
	
	// Get and set simple text as simple strings
	public String getStringText() {
		return this.getText().toString();
	}

	public void setStringText(String text) {
		this.setText(text);
	}
	
	// Get and set styled HTML text
	public String getTextHTML() {
		return Html.toHtml(this.getText());
	}

	public void setTextHTML(String text) {
		this.setText(Html.fromHtml(text, imageGetter, null));
	}


	// Set the default image getter that handles the loading of inline images
	public void setImageGetter(Html.ImageGetter imageGetter) {
		this.imageGetter = imageGetter;
	}
	
	// Style toggle button setters
	public void setBoldToggleButton(ToggleButton button) {
		boldToggle = button;
		
		boldToggle.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				toggleStyle(STYLE_BOLD);
			}
		});
	}

	public void setItalicsToggleButton(ToggleButton button) {
		italicsToggle = button;

		italicsToggle.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				toggleStyle(STYLE_ITALIC);
			}
		});
	}
	
	public void setUnderlineToggleButton(ToggleButton button) {
		underlineToggle = button;

		underlineToggle.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				toggleStyle(STYLE_UNDERLINED);
			}
		});
	}
	
	public void setImageInsertButton(View button, final String imageResource) {
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int position = Selection.getSelectionStart(SimpleEditText.this.getText());

				Spanned e = Html.fromHtml("<img src=\"" + imageResource + "\">", imageGetter, null);

				SimpleEditText.this.getText().insert(position, e);
			}
		});
	}
	
	public void setClearButton(View button) {
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SimpleEditText.this.setText("");
			}
		});
	}
	private class SETextWatcher implements TextWatcher{

	

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			//Add style as the user types if a toggle button is enabled
			int position = Selection.getSelectionStart(SimpleEditText.this.getText());
			if (position <0) {
				position =0;
			}
			 if (position > 0) {
			 	CharacterStyle[] appliedStyles = s.getSpans(position - 1, position, CharacterStyle.class);
				
				StyleSpan currentBoldSpan = null;
				StyleSpan currentItalicSpan = null;
				UnderlineSpan currentUnderlineSpan = null;
				
				//Look for possible styles already applied to the entered text 
				for(int i = 0; i < appliedStyles.length; i++){
					if(appliedStyles[i] instanceof StyleSpan){
						if(((StyleSpan) appliedStyles[i]).getStyle() == android.graphics.Typeface.BOLD){
							//Bold style found 
							currentBoldSpan = (StyleSpan)appliedStyles[i];
						}else if(((StyleSpan)appliedStyles[i]).getStyle() == android.graphics.Typeface.ITALIC) {
							//Italic style found
							currentItalicSpan = (StyleSpan)appliedStyles[i];
						}
					}else if (appliedStyles[i] instanceof UnderlineSpan) {
						//Underlined style found
						currentUnderlineSpan = (UnderlineSpan)appliedStyles[i];
					}
				}
				//Handle the bold style toggle button if it's pressent
				if (boldToggle != null) {
					if (boldToggle.isChecked() && currentBoldSpan ==null) {
						// The user switched the bold style button on and the
						// character doesn't have any bold
						// style applied, so we start a new bold style span. The
						// span is inclusive,
						// so any new characters entered right after this one
						// will automatically get this style.
						
						s.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), position - 1, position, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					}else if (!boldToggle.isChecked() && currentBoldSpan != null) {
						// The user switched the bold style button off and the
						// character has bold style applied.
						// We need to remove the old bold style span, and define
						// a new one that end 1 position right
						// before the newly entered character.
						int boldStart = s.getSpanStart(currentBoldSpan);
						int boldEnd = s.getSpanEnd(currentBoldSpan);
						
						s.removeSpan(currentBoldSpan);
						if (boldStart <= (position -1)) {
							s.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), boldStart, position - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
							
						}

						// The old bold style span end after the current cursor
						// position, so we need to define a
						// second newly created style span too, which begins
						// after the newly entered character and
						// ends at the old span's ending position. So we split
						// the span.
						if (boldEnd > position) {
							s.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), position, boldEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
						}
					}
				}
				
				// Handling italics and underlined styles is the same as
				// handling bold styles.
				// Handle the italics style toggle button if it's present
				if (italicsToggle != null && italicsToggle.isChecked() && currentItalicSpan == null) {
					s.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), position - 1, position, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					
				}else if (italicsToggle != null && !italicsToggle.isChecked() && currentItalicSpan != null) {
					int italicStart = s.getSpanStart(currentItalicSpan);
					int italicEnd = s.getSpanEnd(currentItalicSpan);

					s.removeSpan(currentItalicSpan);
					if (italicStart <= (position - 1)) {
						s.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), italicStart, position - 1,
								Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					}

					// Split the span
					if (italicEnd > position) {
						s.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), position, italicEnd,
								Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					}
				}
				
				// Handle the underlined style toggle button if it's present
				if (underlineToggle != null && underlineToggle.isChecked() && currentUnderlineSpan == null) {
					s.setSpan(new UnderlineSpan(), position - 1, position, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				} else if (underlineToggle != null && !underlineToggle.isChecked() && currentUnderlineSpan != null) {
					int underLineStart = s.getSpanStart(currentUnderlineSpan);
					int underLineEnd = s.getSpanEnd(currentUnderlineSpan);

					s.removeSpan(currentUnderlineSpan);
					if (underLineStart <= (position - 1)) {
						s.setSpan(new UnderlineSpan(), underLineStart, position - 1,
								Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					}

					// We need to split the span
					if (underLineEnd > position) {
						s.setSpan(new UnderlineSpan(), position, underLineEnd,
								Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					}
				}
			}
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			//Unused
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			//Unused
		}
		
	}
	
}
