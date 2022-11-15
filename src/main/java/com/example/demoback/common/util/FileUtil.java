package com.example.demoback.common.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.example.demoback.common.exception.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * File工具类，扩展 hutool 工具包
 */
public class FileUtil extends cn.hutool.core.io.FileUtil {

    /**
     * 定义GB的计算常量
     */
    private static final int GB = 1024 * 1024 * 1024;
    /**
     * 定义MB的计算常量
     */
    private static final int MB = 1024 * 1024;
    /**
     * 定义KB的计算常量
     */
    private static final int KB = 1024;

    /**
     * 格式化小数
     */
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    /**
     * MultipartFile转File
     */
    public static File toFile(MultipartFile multipartFile) {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = "." + getExtensionName(fileName);
        File file = null;
        try {
            // 用uuid作为文件名，防止生成的临时文件重复
            file = File.createTempFile(IdUtil.simpleUUID(), prefix);
            // MultipartFile to File
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 删除
     */
    public static void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 获取文件扩展名
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 文件大小转换
     */
    public static String getSize(long size) {
        String resultSize = "";
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = DF.format(size / (float) GB) + "GB   ";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = DF.format(size / (float) MB) + "MB   ";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = DF.format(size / (float) KB) + "KB   ";
        } else {
            resultSize = size + "B   ";
        }
        return resultSize;
    }

    /**
     * inputStream 转 File
     */
    public static File inputStreamToFile(InputStream ins, String name) throws Exception {
        File file = new File(System.getProperty("java.io.tmpdir") + name);
        if (file.exists()) {
            return file;
        }
        try (OutputStream os = new FileOutputStream(file)) {
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将文件名解析成文件的上传路径
     * @return 上传到服务器的文件名
     */
    public static File upload(MultipartFile file, String filePath) {
        String suffix = getExtensionName(file.getOriginalFilename());
        checkFile(suffix);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssS");
        String name = getFileNameNoEx(file.getOriginalFilename());
        String nowStr = "-" + format.format(date);
        try {
            String fileName = name + nowStr + "." + suffix;
            String path = filePath + fileName;
            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            //String d = dest.getPath();
            file.transferTo(dest);// 文件写入
            return dest;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String fileToBase64(File file) {
        try (FileInputStream inputFile = new FileInputStream(file)) {
            String base64 = null;
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            base64 = Base64.encode(buffer);
            String encoded = base64.replaceAll("[\\s*\t\n\r]", "");
            return encoded;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 导出excel
     */
    public static void downloadExcel(List<Map<String, Object>> list, HttpServletResponse response) throws IOException {
        String tempPath = System.getProperty("java.io.tmpdir") + IdUtil.fastSimpleUUID() + ".xlsx";
        File file = new File(tempPath);
        BigExcelWriter writer = ExcelUtil.getBigWriter(file);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);
        //response为HttpServletResponse对象
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename=file.xlsx");
        ServletOutputStream out = response.getOutputStream();
        // 终止后删除临时文件
        file.deleteOnExit();
        writer.flush(out, true);
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }

    public static String getFileType(String type) {
        String documents = "txt doc pdf ppt pps xlsx xls";
        String music = "mp3 wav wma mpa ram ra aac aif m4a";
        String video = "avi mpg mpe mpeg asf wmv mov qt rm mp4 flv m4v webm ogv ogg";
        String image = "bmp dib pcp dif wmf gif jpg tif eps psd cdr iff tga pcd mpt png jpeg";
        if (image.indexOf(type) != -1) {
            return "图片";
        } else if (documents.indexOf(type) != -1) {
            return "文档";
        } else if (music.indexOf(type) != -1) {
            return "音乐";
        } else if (video.indexOf(type) != -1) {
            return "视频";
        } else {
            return "其他";
        }
    }

    public static boolean checkFile(String fileName) {
        //设置允许上传文件类型
        String suffixList = "txt,doc,docx,xlsx,xls,zip,dwg,pdf,ppt,pps,gif,jpg,png,bpmn,xml,rar";
        // 获取文件后缀
        String suffix = getExtensionName(fileName);
        if (suffixList.contains(suffix.trim().toLowerCase())) {
            return true;
        }
        throw new BadRequestException("不允许上传此类型文件");
    }

    public static String getFileTypeByMimeType(String type) {
        String mimeType = new MimetypesFileTypeMap().getContentType("." + type);
        return mimeType.split("\\/")[0];
    }

    public static void checkSize(long maxSize, long size) {
        if (size > (maxSize * 1024 * 1024)) {
            throw new BadRequestException("文件超出规定大小");
        }
    }

    /**
     * 判断两个文件是否相同
     */
    public static boolean check(File file1, File file2) {
        String img1Md5 = getMd5(file1);
        String img2Md5 = getMd5(file2);
        return img1Md5.equals(img2Md5);
    }

    /**
     * 判断两个文件是否相同
     */
    public static boolean check(String file1Md5, String file2Md5) {
        return file1Md5.equals(file2Md5);
    }

    private static byte[] getByte(File file) {
        // 得到文件长度
        byte[] b = new byte[(int) file.length()];
        try (InputStream in = new FileInputStream(file)) {
            in.read(b);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return b;
    }

    private static String getMd5(byte[] bytes) {
        // 16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(bytes);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            // 移位 输出字符串
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMd5(File file) {
        return getMd5(getByte(file));
    }

    /**
     * @param response
     * @param file
     * @param outFileName
     * @throws Exception
     */
    public static void download(HttpServletResponse response, File file, String outFileName) {
        if (file != null && file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                // 设置相关格式
                response.setContentType("application/force-download");
                // 设置下载后的文件名以及header
                outFileName = StringUtils.isBlank(outFileName) ? file.getName() : outFileName;
                response.addHeader("Content-disposition", "attachment;fileName=" + URLEncoder.encode(outFileName, "utf-8"));
                // 创建输出对象
                OutputStream os = response.getOutputStream();
                // 常规操作
                byte[] buf = new byte[1024];
                int len;
                while ((len = fis.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new BadRequestException("文件不存在");
        }
    }

    /**
     * 解压zip文件
     *
     * @param targetPath 压缩包路径
     * @param savePath   解压存放路径
     */
    public static boolean unZipFile(String targetPath, String savePath) {

        try {
            ZipUtil.unzip(targetPath, savePath, CharsetUtil.CHARSET_GBK);
        } catch (Exception e) {
            try {
                ZipUtil.unzip(targetPath, savePath, CharsetUtil.CHARSET_UTF_8);
            } catch (Exception e2) {
                try {
                    ZipUtil.unzip(targetPath, savePath, CharsetUtil.CHARSET_ISO_8859_1);
                } catch (Exception e3) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 调用提数微服务工具
     *
     * @param extraValues 自定义参数
     * @param path        目标Excel路径
     * @param id          策略组id
     * @return 是否成功
     */
//    public static boolean loadExcelClientUtil(Map<String, String> extraValues, String path, String id) {
//        boolean result = true;
//        String url = "http://localhost:8090/";
//        DIClient client = new DIClient(url);
//        try {
//            client.runImportGroup(id, path, extraValues, DIClient.IMPORT_DATA_FILE_TYPE_EXCEL);
//        } catch (ClientException e) {
//            e.printStackTrace();
//            result = false;
//        }
//        return result;
//    }

    /**
     * 复制文件
     *
     * @param oldPath 文件路径
     * @param newPath 目标路径
     */
    public static void copyFile(String oldPath, String newPath) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                if (new File(newPath).exists()) {
                    new File(newPath).delete();
                }
                newPath = newPath.replace("/", "\\");
                String dir = newPath.substring(0, newPath.lastIndexOf("\\"));
                File dirfile = new File(dir);
                if (!dirfile.exists()) {
                    dirfile.mkdirs();
                }
                inStream = new FileInputStream(oldPath); // 读入原文件
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fs != null) {
                    fs.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static List<File> loopDirectory(File file, List<File> fileList) {
        File[] lsFile = ls(file.getAbsolutePath());
        for (File tempFile : lsFile) {
            if (tempFile.isDirectory()) {
                fileList.add(tempFile);
                loopDirectory(tempFile, fileList);
            }
        }
        return fileList;
    }

    /**
     * 文件上传类型校验
     * @param fileName 文件名
     */
    public static void checkFileType(String fileName) {
        //设置允许上传文件类型
        String suffixList = "xlsx,xls";
        // 获取文件后缀
        String suffix = getExtensionName(fileName);
        if (!suffixList.contains(suffix.trim().toLowerCase())) {
            throw new BadRequestException("不允许上传此类型文件");
        }
    }
}

