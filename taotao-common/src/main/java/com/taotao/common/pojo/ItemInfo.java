package com.taotao.common.pojo;

public class ItemInfo extends Item {

	public String[] getIamges() {
		
		String image = this.getImage();
		if (image != null) {
			
			String[] images = image.split(",");
			return images;
		}
		return null;
	}
}
