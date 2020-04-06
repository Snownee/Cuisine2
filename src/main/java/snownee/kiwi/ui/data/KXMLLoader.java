package snownee.kiwi.ui.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import net.minecraft.client.resources.ReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class KXMLLoader extends ReloadListener<Map<ResourceLocation, Document>> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int XML_EXTENSION_LENGTH = ".xml".length();
    private final String folder;
    public final Map<ResourceLocation, Document> map = Maps.newHashMap();

    public KXMLLoader(String folder) {
        this.folder = folder;
    }

    @Override
    protected Map<ResourceLocation, Document> prepare(IResourceManager resourceManagerIn, IProfiler profilerIn) {
        Map<ResourceLocation, Document> map = Maps.newHashMap();
        int i = this.folder.length() + 1;

        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            for (ResourceLocation resourcelocation : resourceManagerIn.getAllResourceLocations(this.folder, path -> {
                return path.endsWith(".xml");
            })) {
                String s = resourcelocation.getPath();
                ResourceLocation resourcelocation1 = new ResourceLocation(resourcelocation.getNamespace(), s.substring(i, s.length() - XML_EXTENSION_LENGTH));

                try (IResource iresource = resourceManagerIn.getResource(resourcelocation); InputStream inputstream = iresource.getInputStream(); Reader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8))) {

                    Document doc = docBuilder.parse(inputstream);
                    Element kxml = doc.getDocumentElement();
                    if (kxml == null || !kxml.getTagName().equals("kxml")) {
                        throw new IllegalArgumentException("Cannot find kxml tag from data file " + resourcelocation1);
                    }

                    Document doc1 = map.put(resourcelocation1, doc);
                    if (doc1 != null) {
                        throw new IllegalStateException("Duplicate data file ignored with ID " + resourcelocation1);
                    }
                } catch (IllegalArgumentException | IOException | SAXException jsonparseexception) {
                    LOGGER.error("Couldn't parse data file {} from {}", resourcelocation1, resourcelocation, jsonparseexception);
                }
            }
        } catch (ParserConfigurationException | IllegalStateException e) {
            LOGGER.error("Couldn't initialize KXMLLoader", e);
        }

        return map;
    }

    @Override
    protected void apply(Map<ResourceLocation, Document> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        map.clear();
        map.putAll(objectIn);
    }

    protected ResourceLocation getPreparedPath(ResourceLocation rl) {
        return new ResourceLocation(rl.getNamespace(), this.folder + "/" + rl.getPath() + ".xml");
    }

}
