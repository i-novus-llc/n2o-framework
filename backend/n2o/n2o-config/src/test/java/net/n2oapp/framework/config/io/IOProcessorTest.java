package net.n2oapp.framework.config.io;

import net.n2oapp.engine.factory.EngineNotFoundException;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.io.*;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;
import net.n2oapp.framework.api.metadata.persister.TypedElementPersister;
import net.n2oapp.framework.api.metadata.reader.NamespaceReader;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.config.selective.persister.PersisterFactoryByMap;
import net.n2oapp.framework.config.selective.reader.ReaderFactoryByMap;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.XMLUnit;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.PropertyResolver;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;


/**
 * Тест процессора I/O
 */
public class IOProcessorTest {

    static public class EmptyEntity {

    }


    enum MyEnum {
        en1, en2
    }

    static public class BaseEntity {

        private String attr;
        private ChildEntity childEntity;

        String getAttr() {
            return attr;
        }

        void setAttr(String attr) {
            this.attr = attr;
        }

        public ChildEntity getChildEntity() {
            return childEntity;
        }

        public void setChildEntity(ChildEntity childEntity) {
            this.childEntity = childEntity;
        }
    }

    static public class BoolEntity extends BaseEntity {
        private Boolean boolAttr;

        public Boolean getBoolAttr() {
            return boolAttr;
        }

        public void setBoolAttr(Boolean boolAttr) {
            this.boolAttr = boolAttr;
        }
    }

    static public class ExtAttributesEntity extends BaseEntity implements ExtensionAttributesAware {
        private Map<N2oNamespace, Map<String, String>> extensions;
        private Map<N2oNamespace, Map<String, String>> childExtensions;

        @Override
        public Map<N2oNamespace, Map<String, String>> getExtAttributes() {
            return extensions;
        }

        @Override
        public void setExtAttributes(Map<N2oNamespace, Map<String, String>> extAttributes) {
            this.extensions = extAttributes;
        }

        public Map<N2oNamespace, Map<String, String>> getChildExtensions() {
            return childExtensions;
        }

        public void setChildExtensions(Map<N2oNamespace, Map<String, String>> childExtensions) {
            this.childExtensions = childExtensions;
        }
    }

    static public class IntEntity extends BaseEntity {
        private Integer intAttr;

        public Integer getIntAttr() {
            return intAttr;
        }

        public void setIntAttr(Integer intAttr) {
            this.intAttr = intAttr;
        }
    }

    static public class ArrayEntity extends BaseEntity {
        private String[] arrayAttr;

        public String[] getArrayAttr() {
            return arrayAttr;
        }

        public void setArrayAttr(String[] arrayAttr) {
            this.arrayAttr = arrayAttr;
        }
    }

    static public class EnumEntity extends BaseEntity {
        private MyEnum en;

        MyEnum getEn() {
            return en;
        }

        void setEn(MyEnum en) {
            this.en = en;
        }

    }

    static public class ChildEntity implements NamespaceUriAware {
        private String text;
        private MyEnum en;
        private Boolean bool;
        private Integer intAttr;
        private String attr;
        private ChildEntity child;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public MyEnum getEn() {
            return en;
        }

        public void setEn(MyEnum en) {
            this.en = en;
        }

        public Boolean getBool() {
            return bool;
        }

        public void setBool(Boolean bool) {
            this.bool = bool;
        }

        public Integer getInt() {
            return intAttr;
        }

        public void setInt(Integer intAttr) {
            this.intAttr = intAttr;
        }

        public String getAtt() {
            return attr;
        }

        public void setAtt(String att) {
            this.attr = att;
        }

        public ChildEntity getChild() {
            return child;
        }

        public void setChild(ChildEntity child) {
            this.child = child;
        }

        @Override
        public String getNamespaceUri() {
            return "http://example.com/n2o/ext-1.0";
        }
    }

    static public class ChildrenEntityList {
        private ChildEntity[] childEntities;

        public ChildEntity[] getChildEntities() {
            return childEntities;
        }

        public void setChildEntities(ChildEntity[] childEntities) {
            this.childEntities = childEntities;
        }
    }

    static public class BodyEntity extends BaseEntity {
        private String body;

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }

    static public class ListEnumEntity {
        private EnumEntity[] entityList;

        EnumEntity[] getEntityList() {
            return entityList;
        }

        void setEntityList(EnumEntity[] entityList) {
            this.entityList = entityList;
        }
    }

    static public class ListBaseEntity {
        private BaseEntity[] entityList;

        BaseEntity[] getEntityList() {
            return entityList;
        }

        void setEntityList(BaseEntity[] entityList) {
            this.entityList = entityList;
        }
    }

    static public class NamespaceEntity implements NamespaceUriAware {

        private String attr;
        private String namespaceUri;

        String getAttr() {
            return attr;
        }

        void setAttr(String attr) {
            this.attr = attr;
        }

        @Override
        public String getNamespaceUri() {
            return namespaceUri;
        }

        @Override
        public void setNamespaceUri(String namespaceUri) {
            this.namespaceUri = namespaceUri;
        }
    }

    static public class BodyNamespaceEntity extends NamespaceEntity {
        private String body;

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }

    static public class EnumNamespaceEntity extends NamespaceEntity {
        private MyEnum en;

        MyEnum getEn() {
            return en;
        }

        void setEn(MyEnum en) {
            this.en = en;
        }
    }


    static public class ListNamespaceEntity {
        private NamespaceEntity[] entityList;

        NamespaceEntity[] getEntityList() {
            return entityList;
        }

        void setEntityList(NamespaceEntity[] entityList) {
            this.entityList = entityList;
        }
    }

    static public class MapStringNamespaceEntity {
        private Map<String, String> entityStringMap;

        public Map<String, String> getEntityStringMap() {
            return entityStringMap;
        }

        public void setEntityStringMap(Map<String, String> entityStringMap) {
            this.entityStringMap = entityStringMap;
        }
    }

    static public class MapNamespaceEntity {
        private Map<String, Object> entityMap;

        public Map<String, Object> getEntityMap() {
            return entityMap;
        }

        public void setEntityMap(Map<String, Object> entityMap) {
            this.entityMap = entityMap;
        }
    }

    @Test
    public void testRead() throws Exception {
        new IOProcessorImpl(false).read(dom("net/n2oapp/framework/config/io/ioprocessor1.xml"), new EmptyEntity(), (e, t) -> {
            fail();
        });
    }

    @Test
    public void testPersist() {
        new IOProcessorImpl(true).persist(new EmptyEntity(), new Element("test"), (t, e) -> {
            fail();
        });
    }

    @Test
    public void testExtensionAttributes() throws Exception {
        IOProcessor io = new IOProcessorImpl(true);
        Element in = dom("net/n2oapp/framework/config/io/ioprocessor1.xml");
        ExtAttributesEntity extAttrEntity = new ExtAttributesEntity();
        io.anyAttributes(in, extAttrEntity::getExtAttributes, extAttrEntity::setExtAttributes);
        Assert.assertEquals(2, extAttrEntity.getExtAttributes().size());
        Assert.assertEquals("extAttr1", extAttrEntity.getExtAttributes().get(new N2oNamespace(Namespace.getNamespace("ext", "http://example.com/n2o/ext-2.0"))).get("att1"));
        Assert.assertEquals("ext2Attr1", extAttrEntity.getExtAttributes().get(new N2oNamespace(Namespace.getNamespace("ext2", "http://example.com/n2o/ext-3.0"))).get("att1"));
        Assert.assertEquals("ext2Attr2", extAttrEntity.getExtAttributes().get(new N2oNamespace(Namespace.getNamespace("ext2", "http://example.com/n2o/ext-3.0"))).get("att2"));

        io = new IOProcessorImpl(false);
        Element element = new Element("test");
        io.anyAttributes(element, extAttrEntity::getExtAttributes, extAttrEntity::setExtAttributes);
        Assert.assertEquals(3, element.getAttributes().size());
        Assert.assertEquals("extAttr1", element.getAttributeValue("att1", Namespace.getNamespace("ext", "http://example.com/n2o/ext-2.0")));
        Assert.assertEquals("ext2Attr1", element.getAttributeValue("att1", Namespace.getNamespace("ext2", "http://example.com/n2o/ext-3.0")));
        Assert.assertEquals("ext2Attr2", element.getAttributeValue("att2", Namespace.getNamespace("ext2", "http://example.com/n2o/ext-3.0")));
    }

    @Test
    public void testChildAnyAttributes() throws Exception {
        IOProcessor io = new IOProcessorImpl(true);
        Element in = dom("net/n2oapp/framework/config/io/ioprocessor21.xml");
        ExtAttributesEntity entity = new ExtAttributesEntity();
        io.childAnyAttributes(in, "sub", entity::getChildExtensions, entity::setChildExtensions);
        Assert.assertEquals(2, entity.getChildExtensions().size());
        Assert.assertEquals("extAttr1", entity.getChildExtensions().get(new N2oNamespace(Namespace.getNamespace("ext", "http://example.com/n2o/ext-2.0"))).get("att1"));
        Assert.assertEquals("ext2Attr1", entity.getChildExtensions().get(new N2oNamespace(Namespace.getNamespace("ext2", "http://example.com/n2o/ext-3.0"))).get("att1"));
        Assert.assertEquals("ext2Attr2", entity.getChildExtensions().get(new N2oNamespace(Namespace.getNamespace("ext2", "http://example.com/n2o/ext-3.0"))).get("att2"));


        io = new IOProcessorImpl(false);
        Element element = new Element("test");
        io.childAnyAttributes(element, "sub", entity::getChildExtensions, entity::setChildExtensions);
        Element child = element.getChild("sub", element.getNamespace());
        Assert.assertEquals(3, child.getAttributes().size());
        Assert.assertEquals("extAttr1", child.getAttributeValue("att1", Namespace.getNamespace("ext", "http://example.com/n2o/ext-2.0")));
        Assert.assertEquals("ext2Attr1", child.getAttributeValue("att1", Namespace.getNamespace("ext2", "http://example.com/n2o/ext-3.0")));
        Assert.assertEquals("ext2Attr2", child.getAttributeValue("att2", Namespace.getNamespace("ext2", "http://example.com/n2o/ext-3.0")));
    }


    @Test
    public void testOtherAttributes() throws Exception {
        IOProcessor p = new IOProcessorImpl(true);
        Element in = dom("net/n2oapp/framework/config/io/ioprocessor2.xml");
        Namespace namespace = Namespace.getNamespace("ext", "http://n2oapp.net/framework/ext-1.0");
        Map<String, String> map = new HashMap<>();
        p.otherAttributes(in, namespace, map);
        Assert.assertEquals(2, map.size());
        Assert.assertEquals("v1", map.get("att1"));
        Assert.assertEquals("v2", map.get("att2"));

        p = new IOProcessorImpl(false);
        Element out = new Element("test");
        p.otherAttributes(out, namespace, map);
        assertThat(out.getAttributes().size(), equalTo(2));
        assertThat(out.getAttributeValue("att1", namespace), equalTo("v1"));
        assertThat(out.getAttributeValue("att2", namespace), equalTo("v2"));
    }


    @Test
    public void testAttributeBoolean() throws Exception {
        IOProcessor p = new IOProcessorImpl(true);
        Element in = dom("net/n2oapp/framework/config/io/ioprocessor6.xml");
        BoolEntity boolEntity = new BoolEntity();
        p.attributeBoolean(in, "att1", boolEntity::getBoolAttr, boolEntity::setBoolAttr);
        Assert.assertEquals(true, boolEntity.getBoolAttr());
        p = new IOProcessorImpl(false);
        Namespace namespace = Namespace.getNamespace("http://example.com/n2o/ext-1.0");
        Element out = new Element("test", namespace);
        p.attributeBoolean(out, "att1", boolEntity::getBoolAttr, boolEntity::setBoolAttr);
        assertThat(out.getAttributeValue("att1"), equalTo("true"));
    }

    @Test
    public void testAttributeInteger() throws Exception {
        IOProcessor p = new IOProcessorImpl(true);
        Element in = dom("net/n2oapp/framework/config/io/ioprocessor8.xml");
        IntEntity intEntity = new IntEntity();
        p.attributeInteger(in, "att1", intEntity::getIntAttr, intEntity::setIntAttr);
        Assert.assertEquals((Integer) 7, intEntity.getIntAttr());
        p = new IOProcessorImpl(false);
        Element out = new Element("test", Namespace.getNamespace("http://example.com/n2o/ext-1.0"));
        p.attributeInteger(out, "att1", intEntity::getIntAttr, intEntity::setIntAttr);
        assertThat(out.getAttributeValue("att1"), equalTo("7"));
    }

    @Test
    public void testChildrenToMap() throws Exception {
        ReaderFactoryByMap readerFactory = new ReaderFactoryByMap();
        IOProcessor pR = new IOProcessorImpl(readerFactory);
        readerFactory.register(new BodyNamespaceEntityIO());
        Element in = dom("net/n2oapp/framework/config/io/ioprocessor7.xml");
        MapNamespaceEntity map = new MapNamespaceEntity();
        pR.childrenToMap(in, "children", "el2", "attr", null, map::getEntityMap, map::setEntityMap);
        assertThat(map.getEntityMap().get("1"), equalTo(1));
        assertThat(map.getEntityMap().get("test2"), equalTo("test2"));
        PersisterFactoryByMap persisterFactory = new PersisterFactoryByMap();
        persisterFactory.register(new BodyNamespaceEntityIO());
        IOProcessor pW = new IOProcessorImpl(persisterFactory);
        Element out = new Element("test", Namespace.getNamespace("http://example.com/n2o/ext-1.0"));
        pW.childrenToMap(out, "children", "el2", "attr", null, map::getEntityMap, map::setEntityMap);
        assertThat(in, isSimilarTo(out));

        map.entityMap.clear();
        in = dom("net/n2oapp/framework/config/io/ioprocessor24.xml");
        pR.childrenToMap(in, "children", "el", map::getEntityMap, map::setEntityMap);
        assertThat(map.getEntityMap().get("attr"), equalTo(1));
        assertThat(map.getEntityMap().get("attr1"), equalTo("test1"));
        out = new Element("test", Namespace.getNamespace("http://example.com/n2o/ext-1.0"));
        pW.childrenToMap(out, "children", "el", map::getEntityMap, map::setEntityMap);
        assertThat(in, isSimilarTo(out));
    }

    @Test
    public void testChildrenToMapString() throws Exception {
        ReaderFactoryByMap readerFactory = new ReaderFactoryByMap();
        IOProcessor p = new IOProcessorImpl(readerFactory);
        readerFactory.register(new BodyNamespaceEntityIO());
        Element in = dom("net/n2oapp/framework/config/io/ioprocessor7.xml");
        MapStringNamespaceEntity map = new MapStringNamespaceEntity();
        p.childrenToStringMap(in, "children", "el2", "attr", null, map::getEntityStringMap, map::setEntityStringMap);
        assertThat(map.getEntityStringMap().get("1"), equalTo("1"));
        assertThat(map.getEntityStringMap().get("test2"), equalTo("test2"));
        PersisterFactoryByMap persisterFactory = new PersisterFactoryByMap();
        persisterFactory.register(new BodyNamespaceEntityIO());
        p = new IOProcessorImpl(persisterFactory);
        Element out = new Element("test", Namespace.getNamespace("http://example.com/n2o/ext-1.0"));
        p.childrenToStringMap(out, "children", "el2", "attr", null, map::getEntityStringMap, map::setEntityStringMap);
        assertThat(in, isSimilarTo(out));
    }

    @Test
    public void testChildrenToStringArray() throws JDOMException, IOException {
        IOProcessor p = new IOProcessorImpl(true);
        Element in = dom("net/n2oapp/framework/config/io/ioprocessor20.xml");
        ArrayEntity arrayEntity = new ArrayEntity();
        p.childrenToStringArray(in, "children", "value", arrayEntity::getArrayAttr, arrayEntity::setArrayAttr);
        assertThat(arrayEntity.getArrayAttr()[0], equalTo("1"));
        assertThat(arrayEntity.getArrayAttr()[1], equalTo("test2"));
        PersisterFactoryByMap persisterFactory = new PersisterFactoryByMap();
        persisterFactory.register(new BodyNamespaceEntityIO());
        p = new IOProcessorImpl(persisterFactory);
        Element out = new Element("test");
        p.childrenToStringArray(out, "children", "value", arrayEntity::getArrayAttr, arrayEntity::setArrayAttr);
        assertThat(in, isSimilarTo(out));
    }

    @Test
    public void testAttributeArray() throws Exception {
        IOProcessor p = new IOProcessorImpl(true);
        Element in = dom("net/n2oapp/framework/config/io/ioprocessor9.xml");
        ArrayEntity arrayEntity = new ArrayEntity();
        p.attributeArray(in, "att1", ",", arrayEntity::getArrayAttr, arrayEntity::setArrayAttr);
        Assert.assertEquals(3, arrayEntity.getArrayAttr().length);
        Assert.assertEquals("1", arrayEntity.getArrayAttr()[0]);
        Assert.assertEquals(" 2", arrayEntity.getArrayAttr()[1]);
        Assert.assertEquals("3", arrayEntity.getArrayAttr()[2]);
        p = new IOProcessorImpl(false);
        Element out = new Element("test", Namespace.getNamespace("http://example.com/n2o/ext-1.0"));
        p.attributeArray(out, "att1", ",", arrayEntity::getArrayAttr, arrayEntity::setArrayAttr);
        assertThat(out.getAttributeValue("att1"), equalTo("1, 2,3"));
    }


    @Test
    public void testChildrenByEnum() throws Exception {
        IOProcessor p = new IOProcessorImpl(true);
        Element in = dom("net/n2oapp/framework/config/io/ioprocessor3.xml");
        ListEnumEntity listEnumEntity = new ListEnumEntity();
        p.childrenByEnum(in, null, listEnumEntity::getEntityList, listEnumEntity::setEntityList, EnumEntity::getEn,
                EnumEntity::setEn, EnumEntity::new, MyEnum.class, this::ioBaseEntity);
        assertThat(listEnumEntity.getEntityList().length, equalTo(2));
        assertThat(listEnumEntity.getEntityList()[0].getEn(), equalTo(MyEnum.en1));
        assertThat(listEnumEntity.getEntityList()[0].getAttr(), equalTo("test"));
        assertThat(listEnumEntity.getEntityList()[1].getEn(), equalTo(MyEnum.en2));
        assertThat(listEnumEntity.getEntityList()[1].getAttr(), equalTo("test"));

        p = new IOProcessorImpl(false);
        Element out = new Element("test");
        p.childrenByEnum(out, null, listEnumEntity::getEntityList, listEnumEntity::setEntityList, EnumEntity::getEn,
                EnumEntity::setEn, EnumEntity::new, MyEnum.class, this::ioBaseEntity);
        assertThat(in, isSimilarTo(out));
    }

    @Test
    public void testChildrenText() throws Exception {
        ReaderFactoryByMap readerFactory = new ReaderFactoryByMap();
        IOProcessor p = new IOProcessorImpl(readerFactory);
        readerFactory.register(new BodyNamespaceEntityIO());
        Element in = dom("net/n2oapp/framework/config/io/ioprocessor10.xml");
        ChildEntity childrenEntity = new ChildEntity();
        p.childrenText(in, "el1", childrenEntity::getText, childrenEntity::setText);
        assertThat(childrenEntity.getText(), equalTo("test"));
        PersisterFactoryByMap persisterFactory = new PersisterFactoryByMap();
        persisterFactory.register(new BodyNamespaceEntityIO());
        p = new IOProcessorImpl(persisterFactory);
        Element out = new Element("test", Namespace.getNamespace("http://example.com/n2o/ext-1.0"));
        p.childrenText(out, "el1", childrenEntity::getText, childrenEntity::setText);
        assertThat(in, isSimilarTo(out));
    }

    @Test
    public void testChildrenAttributes() throws Exception {
        ReaderFactoryByMap readerFactory = new ReaderFactoryByMap();
        readerFactory.register(new EnumNamespaceEntityIO());
        readerFactory.register(new BodyNamespaceEntityIO());
        IOProcessor p = new IOProcessorImpl(readerFactory);
        Element in = dom("net/n2oapp/framework/config/io/ioprocessor11.xml");
        ChildEntity childrenEntity = new ChildEntity();
        p.childAttribute(in, "el1", "attr", childrenEntity::getAtt, childrenEntity::setAtt);
        p.childAttributeBoolean(in, "el1", "attr1", childrenEntity::getBool, childrenEntity::setBool);
        p.childAttributeInteger(in, "el1", "attr2", childrenEntity::getInt, childrenEntity::setInt);
        p.childAttributeEnum(in, "el1", "enum1", childrenEntity::getEn, childrenEntity::setEn, MyEnum.class);
        assertThat(childrenEntity.getAtt(), equalTo("test"));
        assertThat(childrenEntity.getBool(), equalTo(true));
        assertThat(childrenEntity.getInt(), equalTo(5));
        assertThat(childrenEntity.getEn(), equalTo(MyEnum.en1));
        PersisterFactoryByMap persisterFactory = new PersisterFactoryByMap();
        persisterFactory.register(new EnumNamespaceEntityIO());
        persisterFactory.register(new BodyNamespaceEntityIO());
        p = new IOProcessorImpl(persisterFactory);
        Element out = new Element("test", Namespace.getNamespace("http://example.com/n2o/ext-1.0"));
        p.childAttribute(out, "el1", "attr", childrenEntity::getAtt, childrenEntity::setAtt);
        p.childAttributeBoolean(out, "el1", "attr1", childrenEntity::getBool, childrenEntity::setBool);
        p.childAttributeInteger(out, "el1", "attr2", childrenEntity::getInt, childrenEntity::setInt);
        p.childAttributeEnum(out, "el1", "enum1", childrenEntity::getEn, childrenEntity::setEn, MyEnum.class);
        assertThat(in, isSimilarTo(out));
    }

    @Test
    public void testElement() throws Exception {
        ReaderFactoryByMap readerFactory = new ReaderFactoryByMap();
        readerFactory.register(new BodyNamespaceEntityIO());
        IOProcessor p = new IOProcessorImpl(readerFactory);
        Element in = dom("net/n2oapp/framework/config/io/ioprocessor17.xml");
        BaseEntity baseEntity = new BaseEntity();
        p.element(in, "attr", baseEntity::getAttr, baseEntity::setAttr);
        assertThat(baseEntity.getAttr(), equalTo("test"));
        PersisterFactoryByMap persisterFactory = new PersisterFactoryByMap();
        persisterFactory.register(new BodyNamespaceEntityIO());
        p = new IOProcessorImpl(persisterFactory);
        Element out = new Element("test", Namespace.getNamespace("http://example.com/n2o/ext-1.0"));
        p.element(out, "attr", baseEntity::getAttr, baseEntity::setAttr);
        assertThat(in, isSimilarTo(out));
    }

    @Test
    public void testProps() throws Exception {
        //test properties
        ReaderFactoryByMap readerFactory = new ReaderFactoryByMap();
        readerFactory.register(new BodyNamespaceEntityIO());
        IOProcessorImpl p = new IOProcessorImpl(readerFactory);
        Properties properties = new Properties();
        properties.setProperty("testProp1", "testProp1");
        PropertyResolver systemProperties = new SimplePropertyResolver(properties);
        p.setSystemProperties(systemProperties);
        testElementWithProperty(p, "testProp1");

        //test params
        HashMap<String, String> params = new HashMap<>();
        params.put("testProp1", "testProp2");
        p = new IOProcessorImpl(readerFactory);
        try {
            MetadataParamHolder.setParams(params);
            testElementWithProperty(p, "testProp2");
        } finally {
            MetadataParamHolder.setParams(null);
        }

        //test messages
        p = new IOProcessorImpl(readerFactory);
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("test_messages");
        messageSource.setDefaultEncoding("UTF-8");
        p.setMessageSourceAccessor(new MessageSourceAccessor(messageSource));
        testElementWithProperty(p, "testProp3");

        //test override
        p = new IOProcessorImpl(readerFactory);
        p.setSystemProperties(systemProperties);
        p.setMessageSourceAccessor(new MessageSourceAccessor(messageSource));
        try {
            MetadataParamHolder.setParams(params);
            testElementWithProperty(p, "testProp2");//самый приоритетный params
        } finally {
            MetadataParamHolder.setParams(null);
        }

        //test fail fast
        p = new IOProcessorImpl(readerFactory);
        p.setFailFast(true);
        //p.setSystemProperties(systemProperties); not set to fail
        try {
            testElementWithProperty(p, "testProp1");
            fail();
        } catch (N2oException ignored) {
        }

        //test fail tolerance
        p = new IOProcessorImpl(readerFactory);
        p.setFailFast(false);
        //p.setSystemProperties(systemProperties); not set to fail
        try {
            Element in = dom("net/n2oapp/framework/config/io/ioprocessor22.xml");
            BaseEntity baseEntity = new BaseEntity();
            p.element(in, "attr", baseEntity::getAttr, baseEntity::setAttr);
            assertThat(baseEntity.getAttr(), equalTo("${testProp1}"));
        } catch (N2oException e) {
            fail();
        }
    }

    private void testElementWithProperty(IOProcessorImpl p, String expectedValue) throws JDOMException, IOException {
        Element in = dom("net/n2oapp/framework/config/io/ioprocessor22.xml");
        BaseEntity baseEntity = new BaseEntity();
        p.element(in, "attr", baseEntity::getAttr, baseEntity::setAttr);
        assertThat(baseEntity.getAttr(), equalTo(expectedValue));
    }

    @Test
    public void testChildren() throws Exception {
        ReaderFactoryByMap readerFactory = new ReaderFactoryByMap();
        readerFactory.register(new BodyNamespaceEntityIO());
        IOProcessor p = new IOProcessorImpl(readerFactory);
        Element in1 = dom("net/n2oapp/framework/config/io/ioprocessor12.xml");
        Element in2 = dom("net/n2oapp/framework/config/io/ioprocessor15.xml");
        ChildrenEntityList childrenEntityList1 = new ChildrenEntityList();
        ChildrenEntityList childrenEntityList2 = new ChildrenEntityList();
        p.children(in1, "children", "el1", childrenEntityList1::getChildEntities, childrenEntityList1::setChildEntities, IOProcessorTest.ChildEntity::new, this::children);
        p.children(in2, null, "el1", childrenEntityList2::getChildEntities, childrenEntityList2::setChildEntities, IOProcessorTest.ChildEntity::new, this::children);
        assertThat(childrenEntityList1.getChildEntities().length, equalTo(3));
        assertThat(childrenEntityList1.getChildEntities()[0].getAtt(), equalTo("test1"));
        assertThat(childrenEntityList1.getChildEntities()[1].getAtt(), equalTo("test2"));
        assertThat(childrenEntityList1.getChildEntities()[2].getAtt(), equalTo("test3"));
        assertThat(childrenEntityList2.getChildEntities().length, equalTo(3));
        assertThat(childrenEntityList2.getChildEntities()[0].getAtt(), equalTo("test1"));
        assertThat(childrenEntityList2.getChildEntities()[1].getAtt(), equalTo("test2"));
        assertThat(childrenEntityList2.getChildEntities()[2].getAtt(), equalTo("test3"));
        PersisterFactoryByMap persisterFactory = new PersisterFactoryByMap();
        persisterFactory.register(new BodyNamespaceEntityIO());
        p = new IOProcessorImpl(persisterFactory);
        Element out1 = new Element("test", Namespace.getNamespace("http://example.com/n2o/ext-1.0"));
        Element out2 = new Element("test", Namespace.getNamespace("http://example.com/n2o/ext-1.0"));
        p.children(out1, "children", "el1", childrenEntityList1::getChildEntities, childrenEntityList1::setChildEntities, IOProcessorTest.ChildEntity::new, this::children);
        p.children(out2, null, "el1", childrenEntityList2::getChildEntities, childrenEntityList2::setChildEntities, IOProcessorTest.ChildEntity::new, this::children);
        assertThat(in1, isSimilarTo(out1));
        assertThat(in2, isSimilarTo(out2));
    }

    @Test
    public void testChild() throws Exception {
        ReaderFactoryByMap readerFactory = new ReaderFactoryByMap();
        readerFactory.register(new BodyNamespaceEntityIO());
        IOProcessor p = new IOProcessorImpl(readerFactory);
        Element in1 = dom("net/n2oapp/framework/config/io/ioprocessor13.xml");
        Element in2 = dom("net/n2oapp/framework/config/io/ioprocessor14.xml");
        ChildEntity childrenEntity1 = new ChildEntity();
        ChildEntity childrenEntity2 = new ChildEntity();
        p.child(in1, "children", "el1", childrenEntity1::getChild, childrenEntity1::setChild, IOProcessorTest.ChildEntity::new, this::children);
        p.child(in2, null, "el1", childrenEntity2::getChild, childrenEntity2::setChild, IOProcessorTest.ChildEntity::new, this::children);
        assertThat(childrenEntity1.getChild().getAtt(), equalTo("test1"));
        assertThat(childrenEntity2.getChild().getAtt(), equalTo("test1"));
        PersisterFactoryByMap persisterFactory = new PersisterFactoryByMap();
        persisterFactory.register(new BodyNamespaceEntityIO());
        p = new IOProcessorImpl(persisterFactory);
        Element out1 = new Element("test", Namespace.getNamespace("http://example.com/n2o/ext-1.0"));
        Element out2 = new Element("test", Namespace.getNamespace("http://example.com/n2o/ext-1.0"));
        p.child(out1, "children", "el1", childrenEntity1::getChild, childrenEntity1::setChild, IOProcessorTest.ChildEntity::new, this::children);
        p.child(out2, null, "el1", childrenEntity2::getChild, childrenEntity2::setChild, IOProcessorTest.ChildEntity::new, this::children);
        assertThat(in1, isSimilarTo(out1));
        assertThat(in2, isSimilarTo(out2));
    }

    private void children(Element e, ChildEntity c, IOProcessor p) {
        p.attribute(e, "attr", c::getAtt, c::setAtt);
    }

    @Test
    public void anyChildren() throws Exception {
        IOProcessor p = new IOProcessorImpl(true);
        Element in = dom("net/n2oapp/framework/config/io/ioprocessor4.xml");
        ListBaseEntity listBaseEntity = new ListBaseEntity();
        ElementIOFactory<BaseEntity,
                TypedElementReader<? extends BaseEntity>,
                TypedElementPersister<? super BaseEntity>> factory = p.oneOf(BaseEntity.class)
                .add("el1", EnumEntity.class, this::ioEnumEntity)
                .add(new TypedElementIO<BodyEntity>() {

                    @Override
                    public void io(Element e, BodyEntity t, IOProcessor p) {
                        ioBodyEntity(e, t, p);
                    }

                    @Override
                    public String getElementName() {
                        return "el2";
                    }

                    @Override
                    public Class<BodyEntity> getElementClass() {
                        return BodyEntity.class;
                    }
                });
        p.anyChildren(in, "children", listBaseEntity::getEntityList, listBaseEntity::setEntityList, factory);
        assertThat(listBaseEntity.entityList.length, equalTo(2));
        assertThat(listBaseEntity.entityList[0].getClass(), equalTo(EnumEntity.class));
        assertThat(listBaseEntity.entityList[1].getClass(), equalTo(BodyEntity.class));
        assertThat(listBaseEntity.entityList[0].getAttr(), equalTo("1"));
        assertThat(listBaseEntity.entityList[1].getAttr(), equalTo("2"));
        assertThat(((EnumEntity) listBaseEntity.entityList[0]).getEn(), equalTo(MyEnum.en1));
        assertThat(((BodyEntity) listBaseEntity.entityList[1]).getBody(), equalTo("test"));
        p = new IOProcessorImpl(false);
        Element out = new Element("test");
        p.anyChildren(out, "children", listBaseEntity::getEntityList, listBaseEntity::setEntityList, factory);
        assertThat(in, isSimilarTo(out));
    }

    @Test
    public void testAnyChild() throws Exception {
        IOProcessor p = new IOProcessorImpl(true);
        Element in1 = dom("net/n2oapp/framework/config/io/ioprocessor18.xml");
        Element in2 = dom("net/n2oapp/framework/config/io/ioprocessor19.xml");
        BaseEntity childEntity1 = new BaseEntity();
        BaseEntity childEntity2 = new BaseEntity();
        NamespaceIOFactory<ChildEntity, NamespaceReader<ChildEntity>, NamespacePersister<ChildEntity>> factory
                = new NamespaceIOFactory<ChildEntity, NamespaceReader<ChildEntity>, NamespacePersister<ChildEntity>>() {
            @Override
            public Class<ChildEntity> getBaseElementClass() {
                return ChildEntity.class;
            }

            @Override
            public NamespaceIOFactory<ChildEntity, NamespaceReader<ChildEntity>, NamespacePersister<ChildEntity>> add(NamespaceIO<? extends ChildEntity> nio) {
                return this;
            }

            @Override
            public NamespaceIOFactory<ChildEntity, NamespaceReader<ChildEntity>, NamespacePersister<ChildEntity>> ignore(String... ignore) {
                return this;
            }

            @Override
            public NamespacePersister<ChildEntity> produce(Class<ChildEntity> clazz, Namespace... namespace) {
                return new NamespacePersister<ChildEntity>() {
                    @Override
                    public Class<ChildEntity> getElementClass() {
                        return ChildEntity.class;
                    }

                    @Override
                    public String getElementName() {
                        return null;
                    }

                    @Override
                    public Element persist(ChildEntity entity, Namespace namespace) {
                        Element element = new Element("el1");
                        element.setAttribute(new Attribute("attr", entity.getAtt()));
                        return element;
                    }

                    @Override
                    public String getNamespaceUri() {
                        return "http://n2oapp.net/framework/ext-1.0";
                    }
                };
            }

            @Override
            public void add(NamespacePersister<ChildEntity> persister) {

            }

            @Override
            public NamespaceReader<ChildEntity> produce(String elementName, Namespace... namespace) {
                return new NamespaceReader<ChildEntity>() {
                    @Override
                    public Class<ChildEntity> getElementClass() {
                        return ChildEntity.class;
                    }

                    @Override
                    public String getElementName() {
                        return null;
                    }

                    @Override
                    public ChildEntity read(Element element) {
                        ChildEntity entity = new ChildEntity();
                        entity.setAtt(element.getAttribute("attr").getValue());
                        return entity;
                    }

                    @Override
                    public String getNamespaceUri() {
                        return "http://n2oapp.net/framework/ext-1.0";
                    }
                };
            }

            @Override
            public void add(NamespaceReader<ChildEntity> reader) {

            }
        };
        p.anyChild(in1, "children", childEntity1::getChildEntity, childEntity1::setChildEntity, factory, null);
        p.anyChild(in2, null, childEntity2::getChildEntity, childEntity2::setChildEntity, factory, null);
        assertThat(childEntity1.getChildEntity().getAtt(), equalTo("test"));
        assertThat(childEntity2.getChildEntity().getAtt(), equalTo("test"));
        p = new IOProcessorImpl(false);
        Element out1 = new Element("test", Namespace.getNamespace("http://example.com/n2o/ext-1.0"));
        Element out2 = new Element("test", Namespace.getNamespace("http://example.com/n2o/ext-1.0"));
        p.anyChild(out1, "children", childEntity1::getChildEntity, childEntity1::setChildEntity, factory, null);
        p.anyChild(out2, null, childEntity2::getChildEntity, childEntity2::setChildEntity, factory, null);
        assertThat(in1, isSimilarTo(out1));
        assertThat(in2, isSimilarTo(out2));

        p = new IOProcessorImpl(new NamespaceReaderFactory() {
            @Override
            public NamespaceReader produce(String elementName, Namespace... namespace) {
                if ("elem1".equals(elementName))
                    throw new EngineNotFoundException(elementName);

                return new NamespaceReader() {
                    @Override
                    public Class getElementClass() {
                        return ChildEntity.class;
                    }

                    @Override
                    public String getElementName() {
                        return null;
                    }

                    @Override
                    public String getNamespaceUri() {
                        return null;
                    }

                    @Override
                    public Object read(Element element) {
                        ChildEntity entity = new ChildEntity();
                        entity.setAtt(element.getName());
                        return entity;
                    }
                };
            }

            @Override
            public void add(NamespaceReader reader) {

            }
        });
        Element in3 = dom("net/n2oapp/framework/config/io/ioprocessor23.xml");
        BaseEntity entity = new BaseEntity();
        try {

            p.anyChild(in3, null, entity::getChildEntity, entity::setChildEntity, p.anyOf(ChildEntity.class), Namespace.getNamespace("http://example.com/n2o/ext-1.0"));
            assert false;
        } catch (EngineNotFoundException e) {
            assertThat(e.getMessage(), is("Engine for 'elem1' not found"));
        }
        p.anyChild(in3, null, entity::getChildEntity, entity::setChildEntity, p.anyOf(ChildEntity.class).ignore("elem1", "elem2"), null);
        assertThat(entity.getChildEntity().getAtt(), is("elem3"));
        p.anyChild(in3, null, entity::getChildEntity, entity::setChildEntity, p.anyOf(ChildEntity.class).ignore("elem1", "elem3"), null);
        assertThat(entity.getChildEntity().getAtt(), is("elem2"));

    }

    @Test
    public void anyChildrenByNamespace() throws Exception {
        ReaderFactoryByMap readerFactory = new ReaderFactoryByMap();
        readerFactory.register(new EnumNamespaceEntityIO());
        readerFactory.register(new BodyNamespaceEntityIO());
        IOProcessor p = new IOProcessorImpl(readerFactory);
        Element in = dom("net/n2oapp/framework/config/io/ioprocessor5.xml");
        ListNamespaceEntity listNamespaceEntity = new ListNamespaceEntity();
        p.anyChildren(in, "children", listNamespaceEntity::getEntityList, listNamespaceEntity::setEntityList,
                p.anyOf(NamespaceEntity.class), Namespace.getNamespace("http://example.com/n2o/ext-1.0"));
        assertThat(listNamespaceEntity.entityList.length, equalTo(2));
        assertThat(listNamespaceEntity.entityList[0].getClass(), equalTo(EnumNamespaceEntity.class));
        assertThat(listNamespaceEntity.entityList[1].getClass(), equalTo(BodyNamespaceEntity.class));
        assertThat(listNamespaceEntity.entityList[0].getAttr(), equalTo("1"));
        assertThat(listNamespaceEntity.entityList[1].getAttr(), equalTo("2"));
        assertThat(((EnumNamespaceEntity) listNamespaceEntity.entityList[0]).getEn(), equalTo(MyEnum.en1));
        assertThat(((BodyNamespaceEntity) listNamespaceEntity.entityList[1]).getBody(), equalTo("test"));

        PersisterFactoryByMap persisterFactory = new PersisterFactoryByMap();
        persisterFactory.register(new EnumNamespaceEntityIO());
        persisterFactory.register(new BodyNamespaceEntityIO());
        p = new IOProcessorImpl(persisterFactory);
        Element out = new Element("test", Namespace.getNamespace("http://example.com/n2o/ext-1.0"));
        p.anyChildren(out, "children", listNamespaceEntity::getEntityList, listNamespaceEntity::setEntityList,
                p.anyOf(NamespaceEntity.class), Namespace.getNamespace("http://example.com/n2o/ext-1.0"));
        assertThat(in, isSimilarTo(out));
    }

    private Element dom(String file) throws JDOMException, IOException {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(file)) {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(inputStream);
            return doc.getRootElement();
        }
    }

    private void ioBaseEntity(Element e, BaseEntity t, IOProcessor p) {
        p.attribute(e, "attr1", t::getAttr, t::setAttr);
    }

    private void ioBodyEntity(Element e, BodyEntity t, IOProcessor p) {
        ioBaseEntity(e, t, p);
        p.text(e, t::getBody, t::setBody);
    }

    private void ioEnumEntity(Element e, EnumEntity t, IOProcessor p) {
        ioBaseEntity(e, t, p);
        p.attributeEnum(e, "enum1", t::getEn, t::setEn, MyEnum.class);
    }

    static class EnumNamespaceEntityIO implements NamespaceIO<EnumNamespaceEntity> {

        @Override
        public Class<EnumNamespaceEntity> getElementClass() {
            return EnumNamespaceEntity.class;
        }

        @Override
        public String getElementName() {
            return "el1";
        }

        @Override
        public String getNamespaceUri() {
            return "http://example.com/n2o/ext-1.0";
        }

        @Override
        public void io(Element e, EnumNamespaceEntity t, IOProcessor p) {
            p.attribute(e, "attr", t::getAttr, t::setAttr);
            p.attributeEnum(e, "enum1", t::getEn, t::setEn, MyEnum.class);
        }
    }

    static class BodyNamespaceEntityIO implements NamespaceIO<BodyNamespaceEntity> {

        @Override
        public Class<BodyNamespaceEntity> getElementClass() {
            return BodyNamespaceEntity.class;
        }

        @Override
        public String getElementName() {
            return "el2";
        }

        @Override
        public String getNamespaceUri() {
            return "http://example.com/n2o/ext-1.0";
        }

        @Override
        public void io(Element e, BodyNamespaceEntity t, IOProcessor p) {
            p.attribute(e, "attr", t::getAttr, t::setAttr);
            p.text(e, t::getBody, t::setBody);
        }
    }

    private static ElementMatcher isSimilarTo(Element element) {
        return new ElementMatcher(element);
    }

    static class ElementMatcher extends BaseMatcher<Element> {
        private final Element expectedValue;

        public ElementMatcher(Element expectedValue) {
            this.expectedValue = expectedValue;
        }

        @Override
        public boolean matches(Object item) {
            XMLOutputter outputer = new XMLOutputter();
            String expectedXml = outputer.outputString(expectedValue);
            String actualXml = outputer.outputString((Element) item);

            XMLUnit.setIgnoreWhitespace(true);
            XMLUnit.setIgnoreAttributeOrder(true);
            XMLUnit.setIgnoreComments(true);
            XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);

            DetailedDiff diff = null;
            try {
                diff = new DetailedDiff(XMLUnit.compareXML(expectedXml, actualXml));
                return diff.similar();
            } catch (SAXException | IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public void describeTo(Description description) {
            description.appendValue(expectedValue);
        }
    }

}
