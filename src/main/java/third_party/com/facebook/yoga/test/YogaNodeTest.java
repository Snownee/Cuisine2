package third_party.com.facebook.yoga.test;

import org.junit.Assert;
import org.junit.Test;

import third_party.com.facebook.yoga.YogaNode;
import third_party.com.facebook.yoga.YogaValue;

/**
 * Copyright (c) 2014-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

/**
 * Tests for {@link YogaNode}.
 */

//[TestFixture]
public class YogaNodeTest {
    @Test
    public void TestAddChildGetParent() {
        YogaNode parent = new YogaNode();
        YogaNode child = new YogaNode();

        Assert.assertNull(child._owner);
        Assert.assertEquals(0, parent.GetChildCount());

        parent.Insert(0, child);

        Assert.assertEquals(1, parent.GetChildCount());
        Assert.assertEquals(child, parent.GetChild(0));
        Assert.assertEquals(parent, child._owner);

        parent.RemoveAt(0);

        Assert.assertNull(child._owner);
        Assert.assertEquals(0, parent.GetChildCount());
    }

    @Test
    public void TestChildren() {
        YogaNode parent = new YogaNode();
        for (YogaNode node : parent) {
            Assert.fail(node.toString());
        }

        YogaNode child0 = new YogaNode();
        Assert.assertEquals(-1, parent.IndexOf(child0));
        parent.Insert(0, child0);
        for (YogaNode node : parent) {
            Assert.assertEquals(0, parent.IndexOf(node));
        }

        YogaNode child1 = new YogaNode();
        parent.Insert(1, child1);
        int index = 0;
        for (YogaNode node : parent) {
            Assert.assertEquals(index++, parent.IndexOf(node));
        }

        parent.RemoveAt(0);
        Assert.assertEquals(-1, parent.IndexOf(child0));
        Assert.assertEquals(0, parent.IndexOf(child1));

        parent.Clear();
        Assert.assertEquals(0, parent.GetChildCount());

        parent.Clear();
        Assert.assertEquals(0, parent.GetChildCount());
    }

    //    @Test
    //    public void TestRemoveAtFromEmpty()
    //        {
    //            YogaNode parent = new YogaNode();
    //            Assert.Throws<ArgumentOutOfRangeException>(() -> parent.RemoveAt(0));
    //        }

    //    @Test
    //    public void TestRemoveAtOutOfRange()
    //        {
    //            YogaNode parent = new YogaNode();
    //            YogaNode child = new YogaNode();
    //            parent.Insert(0, child);
    //            Assert.Throws<ArgumentOutOfRangeException>(() => parent.RemoveAt(1));
    //        }

    //    @Test
    //    public void TestCannotAddChildToMultipleParents()
    //        {
    //            YogaNode parent1 = new YogaNode();
    //            YogaNode parent2 = new YogaNode();
    //            YogaNode child = new YogaNode();
    //
    //            parent1.Insert(0, child);
    //            Assert.Throws<InvalidOperationException>(() => parent2.Insert(0, child));
    //        }

    @Test

    public void TestReset() {
        int instanceCount = YogaNode.GetInstanceCount();
        YogaNode node = new YogaNode();
        Assert.assertEquals(instanceCount + 1, YogaNode.GetInstanceCount());
        node.Reset();
        Assert.assertEquals(instanceCount + 1, YogaNode.GetInstanceCount());
    }

    //    @Test
    //    public void TestResetParent()
    //        {
    //            YogaNode parent = new YogaNode();
    //            YogaNode child = new YogaNode();
    //            parent.Insert(0, child);
    //            Assert.Throws<InvalidOperationException>(() -> parent.Reset());
    //        }

    //    @Test
    //    public void TestResetChild()
    //        {
    //            YogaNode parent = new YogaNode();
    //            YogaNode child = new YogaNode();
    //            parent.Insert(0, child);
    //            Assert.Throws<InvalidOperationException>(() => child.Reset());
    //        }

    @Test
    public void TestClear() {
        int instanceCount = YogaNode.GetInstanceCount();
        YogaNode parent = new YogaNode();
        Assert.assertEquals(instanceCount + 1, YogaNode.GetInstanceCount());
        YogaNode child = new YogaNode();
        Assert.assertEquals(instanceCount + 2, YogaNode.GetInstanceCount());
        parent.Insert(0, child);
        Assert.assertEquals(1, parent.GetChildCount());
        Assert.assertEquals(parent, child._owner);
        parent.Clear();
        Assert.assertEquals(0, parent.GetChildCount());
        Assert.assertNull(child._owner);
        Assert.assertEquals(instanceCount + 2, YogaNode.GetInstanceCount());
    }

    //    @Test
    //    public void TestMeasureFunc()
    //        {
    //            YogaNode node = new YogaNode();
    //            node.SetMeasureFunction((_, width, widthMode, height, heightMode) => {
    //                return MeasureOutput.Make(100, 150);
    //            });
    //            node.CalculateLayout();
    //            Assert.assertEquals(100, node.LayoutWidth);
    //            Assert.assertEquals(150, node.LayoutHeight);
    //        }

    //    @Test
    //    public void TestMeasureFuncWithFloat()
    //        {
    //            YogaNode node = new YogaNode();
    //            node.SetMeasureFunction((_, width, widthMode, height, heightMode) => {
    //                return MeasureOutput.Make(123.4f, 81.7f);
    //            });
    //            node.CalculateLayout();
    //            Assert.assertEquals(124.0f, node.LayoutWidth);
    //            Assert.assertEquals(82.0f, node.LayoutHeight);
    //
    //            node = new YogaNode(new YogaConfig{PointScaleFactor = 0});
    //            node.SetMeasureFunction((_, width, widthMode, height, heightMode) => {
    //                return MeasureOutput.Make(123.4f, 81.7f);
    //            });
    //            node.CalculateLayout();
    //            Assert.assertEquals(123.4f, node.LayoutWidth);
    //            Assert.assertEquals(81.7f, node.LayoutHeight);
    //        }

    //    @Test
    //    public void TestChildWithMeasureFunc()
    //        {
    //            YogaNode node = new YogaNode();
    //            node.SetMeasureFunction((_, width, widthMode, height, heightMode) => {
    //                return MeasureOutput.Make(100, 150);
    //            });
    //            YogaNode child = new YogaNode();
    //            Assert.Throws<InvalidOperationException>(() => node.Insert(0, child));
    //        }

    //    @Test
    //    public void TestMeasureFuncWithChild()
    //        {
    //            YogaNode node = new YogaNode();
    //            YogaNode child = new YogaNode();
    //            node.Insert(0, child);
    //            Assert.Throws<InvalidOperationException>(() =>
    //                node.SetMeasureFunction((_, width, widthMode, height, heightMode) => {
    //                    return MeasureOutput.Make(100, 150);
    //                })
    //            );
    //        }

    //    @Test
    //    public void TestBaselineFunc()
    //        {
    //            YogaNode node = new YogaNode();
    //            node.SetHeight(YogaValue.Pt(200;
    //            node.FlexDirection = YogaFlexDirection.Row;
    //            node.AlignItems = YogaAlign.Baseline;
    //
    //            YogaNode child0 = new YogaNode();
    //            child0.Width = 100;
    //            child0.SetHeight(YogaValue.Pt(110;
    //            child0.SetBaselineFunction((_, width, height) => {
    //                Assert.assertEquals(100, width);
    //                Assert.assertEquals(110, height);
    //                return 65;
    //            });
    //            node.Insert(0, child0);
    //
    //            YogaNode child1 = new YogaNode();
    //            child1.Width = 100;
    //            child1.SetHeight(YogaValue.Pt(110;
    //            child1.SetBaselineFunction((_, width, height) => {
    //                Assert.assertEquals(100, width);
    //                Assert.assertEquals(110, height);
    //                return 80;
    //            });
    //            node.Insert(1, child1);
    //
    //            YogaNode child2 = new YogaNode();
    //            child2.Width = 100;
    //            child2.SetHeight(YogaValue.Pt(110;
    //            child2.SetBaselineFunction((_, width, height) => {
    //                Assert.assertEquals(100, width);
    //                Assert.assertEquals(110, height);
    //                return 88;
    //            });
    //            node.Insert(2, child2);
    //
    //            node.CalculateLayout();
    //
    //            Assert.assertEquals(0, child0.LayoutX);
    //            Assert.assertEquals(23, child0.LayoutY);
    //            Assert.assertEquals(100, child1.LayoutX);
    //            Assert.assertEquals(8, child1.LayoutY);
    //            Assert.assertEquals(200, child2.LayoutX);
    //            Assert.assertEquals(0, child2.LayoutY);
    //        }

    //@Test
    //public void TestPrintOneNode()
    //{
    //    YogaNode node = new YogaNode();
    //    node.Width = 100;
    //    node.SetHeight(YogaValue.Pt(120;
    //    node.CalculateLayout();
    //    Assert.assertEquals("<div layout=\"width: 100; height: 120; top: 0; left: 0;\" style=\"width: 100px; height: 120px; \" ></div>", node.Print());
    //}

    //@Test
    //public void TestPrintWithLogger()
    //{
    //    YogaNode node = new YogaNode(new YogaConfig{Logger = (c, n, l, m) => {}});
    //    node.Width = 110;
    //    node.SetHeight(YogaValue.Pt(105;
    //    node.CalculateLayout();
    //    Assert.assertEquals("<div layout=\"width: 110; height: 105; top: 0; left: 0;\" style=\"width: 110px; height: 105px; \" ></div>", node.Print());
    //}

    //@Test
    //public void TestPrint()
    //{
    //    YogaNode parent = new YogaNode();
    //    parent.Width = 100;
    //    parent.SetHeight(YogaValue.Pt(120;
    //    YogaNode child0 = new YogaNode();
    //    child0.Width = 30;
    //    child0.SetHeight(YogaValue.Pt(40;
    //    YogaNode child1 = new YogaNode();
    //    child1.Width = 35;
    //    child1.SetHeight(YogaValue.Pt(45;
    //    parent.Insert(0, child0);
    //    parent.Insert(0, child1);
    //    parent.CalculateLayout();
    //    Assert.assertEquals("<div layout=\"width: 100; height: 120; top: 0; left: 0;\" style=\"width: 100px; height: 120px; \" >\n  <div layout=\"width: 35; height: 45; top: 0; left: 0;\" style=\"width: 35px; height: 45px; \" ></div>\n  <div layout=\"width: 30; height: 40; top: 45; left: 0;\" style=\"width: 30px; height: 40px; \" ></div>\n</div>", parent.Print());
    //}

    //@Test
    //public void TestCopyStyle()
    //{
    //    YogaNode node0 = new YogaNode();
    //    Assert.IsTrue(YogaConstants.IsUndefined(node0.MaxHeight));

    //    YogaNode node1 = new YogaNode();
    //    node1.MaxHeight = 100;

    //    node0.CopyStyle(node1);
    //    Assert.assertEquals(100.Pt(), node0.MaxHeight);
    //}

    //    @Test
    //    public void TestCopyConstructor()
    //        {
    //            YogaNode node0 = new YogaNode();
    //            node0.MaxWidth = 80;
    //
    //            YogaNode node1 = new YogaNode(node0);
    //            Assert.assertEquals(80.Pt(), node1.MaxWidth);
    //
    //            YogaNode node2 = new YogaNode(node1)
    //            {
    //                MaxHeight = 90,
    //            };
    //            Assert.assertEquals(80.Pt(), node2.MaxWidth);
    //            Assert.assertEquals(90.Pt(), node2.MaxHeight);
    //
    //            YogaNode node3 = new YogaNode(node0)
    //            {
    //                MaxWidth = 100,
    //            };
    //            Assert.assertEquals(100.Pt(), node3.MaxWidth);
    //
    //            YogaNode node4 = new YogaNode(node2)
    //            {
    //                MaxWidth = 100,
    //            };
    //            Assert.assertEquals(100.Pt(), node4.MaxWidth);
    //            Assert.assertEquals(90.Pt(), node4.MaxHeight);
    //        }

    //    #if!UNITY_5_4_OR_NEWER
    //
    //    public static void ForceGC() {
    //        GC.Collect();
    //        GC.WaitForPendingFinalizers();
    //    }

    //    @Test
    //    public void TestDestructor() {
    //        ForceGC();
    //        int instanceCount = YogaNode.GetInstanceCount();
    //        TestDestructorForGC(instanceCount);
    //        ForceGC();
    //        Assert.assertEquals(instanceCount, YogaNode.GetInstanceCount());
    //    }

    //    private void TestDestructorForGC(int instanceCount) {
    //        YogaNode node = new YogaNode();
    //        Assert.assertNotNull(node);
    //        Assert.assertEquals(instanceCount + 1, YogaNode.GetInstanceCount());
    //        node = null;
    //    }

    //    @Test
    //    public void TestDestructorWithChildren() {
    //        ForceGC();
    //        int instanceCount = YogaNode.GetInstanceCount();
    //        TestDestructorWithChildrenForGC1(instanceCount);
    //        ForceGC();
    //        Assert.assertEquals(instanceCount, YogaNode.GetInstanceCount());
    //    }
    //
    //    private void TestDestructorWithChildrenForGC1(int instanceCount) {
    //        YogaNode node = new YogaNode();
    //        Assert.assertEquals(instanceCount + 1, YogaNode.GetInstanceCount());
    //
    //        TestDestructorWithChildrenForGC2(node, instanceCount + 1);
    //        ForceGC();
    //        Assert.assertEquals(instanceCount + 2, YogaNode.GetInstanceCount());
    //
    //        TestDestructorWithChildrenForGC2(node, instanceCount + 2);
    //        ForceGC();
    //        Assert.assertEquals(instanceCount + 3, YogaNode.GetInstanceCount());
    //
    //        node = null;
    //    }
    //
    //    private void TestDestructorWithChildrenForGC2(YogaNode parent, int instanceCount) {
    //        YogaNode child = new YogaNode();
    //        Assert.assertEquals(instanceCount + 1, YogaNode.GetInstanceCount());
    //
    //        parent.Insert(0, child);
    //        child = null;
    //    }
    //
    //    #if YOGA_ENABLE_GC_TEST
    //
    //    @Test
    //
    //    public void TestParentDestructor() {
    //        ForceGC();
    //        int instanceCount = YogaNode.GetInstanceCount();
    //        YogaNode child = new YogaNode();
    //        Assert.assertEquals(instanceCount + 1, YogaNode.GetInstanceCount());
    //
    //        TestParentDestructorForGC(child, instanceCount + 1);
    //        ForceGC();
    //
    //        Assert.assertNull(child._owner);
    //        Assert.assertEquals(instanceCount + 1, YogaNode.GetInstanceCount());
    //    }
    //
    //    private void TestParentDestructorForGC(YogaNode child, int instanceCount) {
    //        YogaNode parent = new YogaNode();
    //        Assert.assertEquals(instanceCount + 1, YogaNode.GetInstanceCount());
    //        parent.Insert(0, child);
    //    }#endif
    //
    //    @Test
    //    public void TestClearWithChildDestructor() {
    //        ForceGC();
    //        int instanceCount = YogaNode.GetInstanceCount();
    //        YogaNode node = new YogaNode();
    //        Assert.assertEquals(instanceCount + 1, YogaNode.GetInstanceCount());
    //        TestClearWithChildDestructorForGC(node, instanceCount + 1);
    //        ForceGC();
    //        Assert.assertEquals(instanceCount + 2, YogaNode.GetInstanceCount());
    //        node.Clear();
    //        Assert.assertEquals(0, node.GetChildCount());
    //        ForceGC();
    //        Assert.assertEquals(instanceCount + 1, YogaNode.GetInstanceCount());
    //    }
    //
    //    private void TestClearWithChildDestructorForGC(YogaNode parent, int instanceCount) {
    //        YogaNode child = new YogaNode();
    //        Assert.assertEquals(instanceCount + 1, YogaNode.GetInstanceCount());
    //        parent.Insert(0, child);
    //    }
    //
    //    @Test
    //
    //    public void TestMeasureFuncWithDestructor() {
    //        ForceGC();
    //        int instanceCount = YogaNode.GetInstanceCount();
    //        YogaNode parent = new YogaNode();
    //        Assert.assertEquals(instanceCount + 1, YogaNode.GetInstanceCount());
    //        TestMeasureFuncWithDestructorForGC(parent);
    //        ForceGC();
    //        Assert.assertEquals(instanceCount + 2, YogaNode.GetInstanceCount());
    //        parent.CalculateLayout();
    //        Assert.assertEquals(120, (int) parent.LayoutWidth);
    //        Assert.assertEquals(130, (int) parent.LayoutHeight);
    //    }
    //
    //    private void TestMeasureFuncWithDestructorForGC(YogaNode parent)
    //        {
    //            YogaNode child = new YogaNode();
    //            parent.Insert(0, child);
    //            child.SetMeasureFunction((_, width, widthMode, height, heightMode) => {
    //                return MeasureOutput.Make(120, 130);
    //            });
    //        }#endif

    @Test
    public void TestLayoutMargin() {
        YogaNode node = new YogaNode();
        node.SetWidth(YogaValue.Pt(100));
        node.SetHeight(YogaValue.Pt(100));
        node.SetMarginStart(YogaValue.Pt(1));
        node.SetMarginEnd(YogaValue.Pt(2));
        node.SetMarginTop(YogaValue.Pt(3));
        node.SetMarginBottom(YogaValue.Pt(4));
        node.CalculateLayout();

        Assert.assertEquals(1, node.GetLayoutMarginLeft(), 0.01f);
        Assert.assertEquals(2, node.GetLayoutMarginRight(), 0.01f);
        Assert.assertEquals(3, node.GetLayoutMarginTop(), 0.01f);
        Assert.assertEquals(4, node.GetLayoutMarginBottom(), 0.01f);
    }

    @Test
    public void TestLayoutPadding() {
        YogaNode node = new YogaNode();
        node.SetWidth(YogaValue.Pt(100));
        node.SetHeight(YogaValue.Pt(100));
        node.SetPaddingStart(YogaValue.Pt(1));
        node.SetPaddingEnd(YogaValue.Pt(2));
        node.SetPaddingTop(YogaValue.Pt(3));
        node.SetPaddingBottom(YogaValue.Pt(4));
        node.CalculateLayout();

        Assert.assertEquals(1, node.GetLayoutPaddingLeft(), 0.01f);
        Assert.assertEquals(2, node.GetLayoutPaddingRight(), 0.01f);
        Assert.assertEquals(3, node.GetLayoutPaddingTop(), 0.01f);
        Assert.assertEquals(4, node.GetLayoutPaddingBottom(), 0.01f);
    }
}
