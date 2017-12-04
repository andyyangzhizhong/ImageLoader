package cn.andy.study.imageloader.imageloader;


import cn.andy.study.imageloader.imageloader.cach.Encoder;

/**
 * 图片加载参数配置
 *
 * @author zdxing
 */
public class ImageLoadOptions {
    private String url;

    private String filePath;

    /** 图片加载结果参数 */
    private ImageLoadSize imageLoadSize;

    /** 是否使用图片缓存 */
    private boolean isMemoryCacheEnable = true;

    /** 是否使用硬盘缓存 */
    private boolean isDiskCacheEnable = false;

    private ImageLoadOptions() {

    }

    public static String getEncodeKey(String filepath) {
        return Encoder.encodeBySHA1(filepath);
    }

    public String getKey() {
        int width = imageLoadSize.getWidth();
        int height = imageLoadSize.getHeight();

        String key = getEncodeKey(filePath);
        return key + "_width_" + width + "_height_" + height + "_scaletype_" + imageLoadSize.getImageScaleType();
    }

    public boolean hasUrl() {
        return url != null && !url.trim().equals("");
    }

    /**
     * 获取图片处理参数，包括图片高、宽、缩放类型
     *
     * @return 图片的加载大小
     */
    public ImageLoadSize getImageLoadSize() {
        return imageLoadSize;
    }

    /**
     * 图片加载时时候使用内存缓存
     *
     * @return true：使用内存缓存
     */
    public boolean isMemoryCacheEnable() {
        return isMemoryCacheEnable;
    }

    /**
     * 图片加载的时候是否使用磁盘缓存
     *
     * @return true：使用图片缓存
     */
    public boolean isDiskCacheEnable() {
        return isDiskCacheEnable;
    }

    public String getUrl() {
        return url;
    }

    public String getFilePath() {
        return filePath;
    }

    public static class Builder {
        private ImageLoadOptions imageLoadOptions;

        public Builder(String filePath, String url) {
            imageLoadOptions = new ImageLoadOptions();
            imageLoadOptions.filePath = filePath;
            imageLoadOptions.url = url;
        }

        public Builder(String filePath) {
            this(filePath, "");
        }

        /**
         * 设置图片裁剪类型
         *
         * @param imageLoadSize 图片裁剪类型
         */
        public Builder setImageLoadSize(ImageLoadSize imageLoadSize) {
            if (imageLoadSize == null) {
                throw new NullPointerException("图片加载，imageLoadSize == null");
            }

            imageLoadOptions.imageLoadSize = imageLoadSize;
            return this;
        }

        /**
         * 设置是否使用内存缓存
         *
         * @param isMemoryCacheEnable true:使用内存缓存,false:不实用内存缓存
         */
        public Builder setMemoryCacheEnable(boolean isMemoryCacheEnable) {
            imageLoadOptions.isMemoryCacheEnable = isMemoryCacheEnable;
            return this;
        }

        /**
         * 设置是否使用磁盘缓存
         *
         * @param isDiskCacheEnable true:使用磁盘缓存,false:不使用磁盘缓存
         */
        public Builder setDiskCacheEnable(boolean isDiskCacheEnable) {
            imageLoadOptions.isDiskCacheEnable = isDiskCacheEnable;
            return this;
        }

        /**
         * 创建图片加载参数
         */
        public ImageLoadOptions build() {
            if (imageLoadOptions.imageLoadSize == null) {
                imageLoadOptions.imageLoadSize = new ImageLoadSize();
            }

            if (imageLoadOptions.filePath == null || imageLoadOptions.filePath.trim().equals("")) {
                throw new IllegalArgumentException("图片加载，必须设置filePath");
            }

            return imageLoadOptions;
        }

        /** 设置图片网络地址 */
        public Builder setUrl(String url) {
            if (url == null) {
                url = "";
            }
            imageLoadOptions.url = url;
            return this;
        }
    }
}
