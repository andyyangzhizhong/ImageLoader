package cn.andy.study.imageloader.imageloader;

/** 图片缩放类型 */
public enum ImageScaleType {
    /** 缩放图片，压缩xy，使图片适应width和width */
    FIT_XY("fit_xy"),

    /** 裁剪图片 */
    CENTER_CROP("center_crop"),

    /** 使图片全部显示 */
    CENTER_INSIDE("center_inside");

    private String value;

    ImageScaleType(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
