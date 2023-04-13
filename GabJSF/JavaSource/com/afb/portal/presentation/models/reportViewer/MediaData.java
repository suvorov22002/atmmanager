package com.afb.portal.presentation.models.reportViewer;

import java.awt.Color;

import java.awt.Font;

import java.io.Serializable;


public class MediaData implements Serializable{


    private static final long serialVersionUID = 1L;

    private Integer Width=780;

    private Integer Height=700;

    private Color Background=new Color(190, 214, 248);

    private Color DrawColor=new Color(0,0,0);

    private Font font = new Font("Serif", Font.TRUETYPE_FONT, 30);

    public MediaData(){}

	public Integer getWidth() {
		return Width;
	}

	public void setWidth(Integer width) {
		Width = width;
	}

	public Integer getHeight() {
		return Height;
	}

	public void setHeight(Integer height) {
		Height = height;
	}

	public Color getBackground() {
		return Background;
	}

	public void setBackground(Color background) {
		Background = background;
	}

	public Color getDrawColor() {
		return DrawColor;
	}

	public void setDrawColor(Color drawColor) {
		DrawColor = drawColor;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}


}