package com.staxrt.tutorial.service.impl;

import com.staxrt.tutorial.model.InfoPaveiksleliui;
import com.staxrt.tutorial.service.PaveikslelisService;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaveikslelisServiceImpl implements PaveikslelisService {

    @Override
    public byte[] sukurtiPaveiksleli(InfoPaveiksleliui info) throws IOException {

        List<String> parts = new ArrayList<>();
        int y = 0;
        int length = info.getTekstas().length();
        for (int i = 0; i < length; i += 100) {
            parts.add(info.getTekstas().substring(i, Math.min(length, i + 100)));
        }
        int y1 = parts.size();

        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);// Represents an image with 8-bit RGBA color components packed into integer pixels.
        Graphics2D graphics2d = image.createGraphics();
        Font font = new Font("TimesNewRoman", Font.BOLD, 48);
        graphics2d.setFont(font);
        FontMetrics fontmetrics = graphics2d.getFontMetrics();
        int width = fontmetrics.stringWidth(parts.get(0));
        int height = fontmetrics.getHeight() * y1;
        graphics2d.dispose();

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics2d = image.createGraphics();
        graphics2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        graphics2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        graphics2d.setFont(font);
        fontmetrics = graphics2d.getFontMetrics();
        graphics2d.setColor(Color.getColor(info.getSpalva(), Color.CYAN));
        for (String part : parts) {
            graphics2d.drawString(part, 0, y +=fontmetrics.getAscent());
        }
        graphics2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);

        return baos.toByteArray();
    }
}
