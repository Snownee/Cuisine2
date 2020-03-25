//package third_party.com.facebook.yoga.test;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import third_party.com.facebook.yoga.YogaConfig;
//import third_party.com.facebook.yoga.YogaDirection;
//import third_party.com.facebook.yoga.YogaNode;
//import third_party.com.facebook.yoga.YogaValue;
//
///**
// * Copyright (c) 2014-present, Facebook, Inc.
// *
// * This source code is licensed under the MIT license found in the
// * LICENSE file in the root directory of this source tree.
// */
//
//// @Generated by gentest/gentest.rb from gentest/fixtures/YGFlexTest.html
//
////[TestFixture]
//public class YGFlexTest {
//    @Test
//    public void Test_flex_basis_flex_grow_column() {
//        YogaConfig config = new YogaConfig();
//
//        YogaNode root = new YogaNode(config);
//        root.SetWidth(YogaValue.Pt(100));
//        root.SetHeight(YogaValue.Pt(100));
//
//        YogaNode root_child0 = new YogaNode(config);
//        root_child0.SetFlexGrow(1);
//        root_child0.SetFlexBasis(YogaValue.Pt(50));
//        root.Insert(0, root_child0);
//
//        YogaNode root_child1 = new YogaNode(config);
//        root_child1.SetFlexGrow(1);
//        root.Insert(1, root_child1);
//        root.SetStyleDirection(YogaDirection.LeftToRight);
//        root.CalculateLayout();
//
//        Assert.assertEquals(0f, root.GetLayoutX(), 0.01f);
//        Assert.assertEquals(0f, root.GetLayoutY(), 0.01f);
//        Assert.assertEquals(100f, root.GetLayoutWidth(), 0.01f);
//        Assert.assertEquals(100f, root.GetLayoutHeight(), 0.01f);
//
//        Assert.assertEquals(0f, root_child0.GetLayoutX(), 0.01f);
//        Assert.assertEquals(0f, root_child0.GetLayoutY(), 0.01f);
//        Assert.assertEquals(100f, root_child0.GetLayoutWidth(), 0.01f);
//        Assert.assertEquals(75f, root_child0.GetLayoutHeight(), 0.01f);
//
//        Assert.assertEquals(0f, root_child1.GetLayoutX(), 0.01f);
//        Assert.assertEquals(75f, root_child1.GetLayoutY(), 0.01f);
//        Assert.assertEquals(100f, root_child1.GetLayoutWidth(), 0.01f);
//        Assert.assertEquals(25f, root_child1.GetLayoutHeight(), 0.01f);
//
//        root.SetStyleDirection(YogaDirection.RightToLeft);
//        root.CalculateLayout();
//
//        Assert.assertEquals(0f, root.GetLayoutX(), 0.01f);
//        Assert.assertEquals(0f, root.GetLayoutY(), 0.01f);
//        Assert.assertEquals(100f, root.GetLayoutWidth(), 0.01f);
//        Assert.assertEquals(100f, root.GetLayoutHeight(), 0.01f);
//
//        Assert.assertEquals(0f, root_child0.GetLayoutX(), 0.01f);
//        Assert.assertEquals(0f, root_child0.GetLayoutY(), 0.01f);
//        Assert.assertEquals(100f, root_child0.GetLayoutWidth(), 0.01f);
//        Assert.assertEquals(75f, root_child0.GetLayoutHeight(), 0.01f);
//
//        Assert.assertEquals(0f, root_child1.GetLayoutX(), 0.01f);
//        Assert.assertEquals(75f, root_child1.GetLayoutY(), 0.01f);
//        Assert.assertEquals(100f, root_child1.GetLayoutWidth(), 0.01f);
//        Assert.assertEquals(25f, root_child1.GetLayoutHeight(), 0.01f);
//    }
//    /*
//        @Test
//        public void Test_flex_basis_flex_grow_row()
//        {
//            YogaConfig config = new YogaConfig();
//
//            YogaNode root = new YogaNode(config);
//            root.FlexDirection = YogaFlexDirection.Row;
//            root.SetWidth(YogaValue.Pt(100;
//            root.SetHeight(YogaValue.Pt(100;
//
//            YogaNode root_child0 = new YogaNode(config);
//            root_child0.SetFlexGrow(1;
//            root_child0.SetFlexGrow(YogaValue.Pt(50;
//            root.Insert(0, root_child0);
//
//            YogaNode root_child1 = new YogaNode(config);
//            root_child1.SetFlexGrow(1;
//            root.Insert(1, root_child1);
//            root.SetStyleDirection(YogaDirection.LeftToRight;
//            root.CalculateLayout();
//
//            Assert.assertEquals(0f, root.GetLayoutX());
//            Assert.assertEquals(0f, root.GetLayoutY());
//            Assert.assertEquals(100f, root.GetLayoutWidth());
//            Assert.assertEquals(100f, root.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child0.GetLayoutX());
//            Assert.assertEquals(0f, root_child0.GetLayoutY());
//            Assert.assertEquals(75f, root_child0.GetLayoutWidth());
//            Assert.assertEquals(100f, root_child0.GetLayoutHeight());
//
//            Assert.assertEquals(75f, root_child1.GetLayoutX());
//            Assert.assertEquals(0f, root_child1.GetLayoutY());
//            Assert.assertEquals(25f, root_child1.GetLayoutWidth());
//            Assert.assertEquals(100f, root_child1.GetLayoutHeight());
//
//            root.SetStyleDirection(YogaDirection.RightToLeft;
//            root.CalculateLayout();
//
//            Assert.assertEquals(0f, root.GetLayoutX());
//            Assert.assertEquals(0f, root.GetLayoutY());
//            Assert.assertEquals(100f, root.GetLayoutWidth());
//            Assert.assertEquals(100f, root.GetLayoutHeight());
//
//            Assert.assertEquals(25f, root_child0.GetLayoutX());
//            Assert.assertEquals(0f, root_child0.GetLayoutY());
//            Assert.assertEquals(75f, root_child0.GetLayoutWidth());
//            Assert.assertEquals(100f, root_child0.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child1.GetLayoutX());
//            Assert.assertEquals(0f, root_child1.GetLayoutY());
//            Assert.assertEquals(25f, root_child1.GetLayoutWidth());
//            Assert.assertEquals(100f, root_child1.GetLayoutHeight());
//        }
//
//        @Test
//        public void Test_flex_basis_flex_shrink_column()
//        {
//            YogaConfig config = new YogaConfig();
//
//            YogaNode root = new YogaNode(config);
//            root.SetWidth(YogaValue.Pt(100;
//            root.SetHeight(YogaValue.Pt(100;
//
//            YogaNode root_child0 = new YogaNode(config);
//            root_child0.FlexShrink = 1;
//            root_child0.SetFlexGrow(YogaValue.Pt(100;
//            root.Insert(0, root_child0);
//
//            YogaNode root_child1 = new YogaNode(config);
//            root_child1.SetFlexGrow(YogaValue.Pt(50;
//            root.Insert(1, root_child1);
//            root.SetStyleDirection(YogaDirection.LeftToRight;
//            root.CalculateLayout();
//
//            Assert.assertEquals(0f, root.GetLayoutX());
//            Assert.assertEquals(0f, root.GetLayoutY());
//            Assert.assertEquals(100f, root.GetLayoutWidth());
//            Assert.assertEquals(100f, root.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child0.GetLayoutX());
//            Assert.assertEquals(0f, root_child0.GetLayoutY());
//            Assert.assertEquals(100f, root_child0.GetLayoutWidth());
//            Assert.assertEquals(50f, root_child0.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child1.GetLayoutX());
//            Assert.assertEquals(50f, root_child1.GetLayoutY());
//            Assert.assertEquals(100f, root_child1.GetLayoutWidth());
//            Assert.assertEquals(50f, root_child1.GetLayoutHeight());
//
//            root.SetStyleDirection(YogaDirection.RightToLeft;
//            root.CalculateLayout();
//
//            Assert.assertEquals(0f, root.GetLayoutX());
//            Assert.assertEquals(0f, root.GetLayoutY());
//            Assert.assertEquals(100f, root.GetLayoutWidth());
//            Assert.assertEquals(100f, root.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child0.GetLayoutX());
//            Assert.assertEquals(0f, root_child0.GetLayoutY());
//            Assert.assertEquals(100f, root_child0.GetLayoutWidth());
//            Assert.assertEquals(50f, root_child0.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child1.GetLayoutX());
//            Assert.assertEquals(50f, root_child1.GetLayoutY());
//            Assert.assertEquals(100f, root_child1.GetLayoutWidth());
//            Assert.assertEquals(50f, root_child1.GetLayoutHeight());
//        }
//
//        @Test
//        public void Test_flex_basis_flex_shrink_row()
//        {
//            YogaConfig config = new YogaConfig();
//
//            YogaNode root = new YogaNode(config);
//            root.FlexDirection = YogaFlexDirection.Row;
//            root.SetWidth(YogaValue.Pt(100;
//            root.SetHeight(YogaValue.Pt(100;
//
//            YogaNode root_child0 = new YogaNode(config);
//            root_child0.FlexShrink = 1;
//            root_child0.SetFlexGrow(YogaValue.Pt(100;
//            root.Insert(0, root_child0);
//
//            YogaNode root_child1 = new YogaNode(config);
//            root_child1.SetFlexGrow(YogaValue.Pt(50;
//            root.Insert(1, root_child1);
//            root.SetStyleDirection(YogaDirection.LeftToRight;
//            root.CalculateLayout();
//
//            Assert.assertEquals(0f, root.GetLayoutX());
//            Assert.assertEquals(0f, root.GetLayoutY());
//            Assert.assertEquals(100f, root.GetLayoutWidth());
//            Assert.assertEquals(100f, root.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child0.GetLayoutX());
//            Assert.assertEquals(0f, root_child0.GetLayoutY());
//            Assert.assertEquals(50f, root_child0.GetLayoutWidth());
//            Assert.assertEquals(100f, root_child0.GetLayoutHeight());
//
//            Assert.assertEquals(50f, root_child1.GetLayoutX());
//            Assert.assertEquals(0f, root_child1.GetLayoutY());
//            Assert.assertEquals(50f, root_child1.GetLayoutWidth());
//            Assert.assertEquals(100f, root_child1.GetLayoutHeight());
//
//            root.SetStyleDirection(YogaDirection.RightToLeft;
//            root.CalculateLayout();
//
//            Assert.assertEquals(0f, root.GetLayoutX());
//            Assert.assertEquals(0f, root.GetLayoutY());
//            Assert.assertEquals(100f, root.GetLayoutWidth());
//            Assert.assertEquals(100f, root.GetLayoutHeight());
//
//            Assert.assertEquals(50f, root_child0.GetLayoutX());
//            Assert.assertEquals(0f, root_child0.GetLayoutY());
//            Assert.assertEquals(50f, root_child0.GetLayoutWidth());
//            Assert.assertEquals(100f, root_child0.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child1.GetLayoutX());
//            Assert.assertEquals(0f, root_child1.GetLayoutY());
//            Assert.assertEquals(50f, root_child1.GetLayoutWidth());
//            Assert.assertEquals(100f, root_child1.GetLayoutHeight());
//        }
//
//        @Test
//        public void Test_flex_shrink_to_zero()
//        {
//            YogaConfig config = new YogaConfig();
//
//            YogaNode root = new YogaNode(config);
//            root.SetHeight(YogaValue.Pt(75;
//
//            YogaNode root_child0 = new YogaNode(config);
//            root_child0.SetWidth(YogaValue.Pt(50;
//            root_child0.SetHeight(YogaValue.Pt(50;
//            root.Insert(0, root_child0);
//
//            YogaNode root_child1 = new YogaNode(config);
//            root_child1.FlexShrink = 1;
//            root_child1.SetWidth(YogaValue.Pt(50;
//            root_child1.SetHeight(YogaValue.Pt(50;
//            root.Insert(1, root_child1);
//
//            YogaNode root_child2 = new YogaNode(config);
//            root_child2.SetWidth(YogaValue.Pt(50;
//            root_child2.SetHeight(YogaValue.Pt(50;
//            root.Insert(2, root_child2);
//            root.SetStyleDirection(YogaDirection.LeftToRight;
//            root.CalculateLayout();
//
//            Assert.assertEquals(0f, root.GetLayoutX());
//            Assert.assertEquals(0f, root.GetLayoutY());
//            Assert.assertEquals(50f, root.GetLayoutWidth());
//            Assert.assertEquals(75f, root.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child0.GetLayoutX());
//            Assert.assertEquals(0f, root_child0.GetLayoutY());
//            Assert.assertEquals(50f, root_child0.GetLayoutWidth());
//            Assert.assertEquals(50f, root_child0.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child1.GetLayoutX());
//            Assert.assertEquals(50f, root_child1.GetLayoutY());
//            Assert.assertEquals(50f, root_child1.GetLayoutWidth());
//            Assert.assertEquals(0f, root_child1.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child2.GetLayoutX());
//            Assert.assertEquals(50f, root_child2.GetLayoutY());
//            Assert.assertEquals(50f, root_child2.GetLayoutWidth());
//            Assert.assertEquals(50f, root_child2.GetLayoutHeight());
//
//            root.SetStyleDirection(YogaDirection.RightToLeft;
//            root.CalculateLayout();
//
//            Assert.assertEquals(0f, root.GetLayoutX());
//            Assert.assertEquals(0f, root.GetLayoutY());
//            Assert.assertEquals(50f, root.GetLayoutWidth());
//            Assert.assertEquals(75f, root.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child0.GetLayoutX());
//            Assert.assertEquals(0f, root_child0.GetLayoutY());
//            Assert.assertEquals(50f, root_child0.GetLayoutWidth());
//            Assert.assertEquals(50f, root_child0.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child1.GetLayoutX());
//            Assert.assertEquals(50f, root_child1.GetLayoutY());
//            Assert.assertEquals(50f, root_child1.GetLayoutWidth());
//            Assert.assertEquals(0f, root_child1.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child2.GetLayoutX());
//            Assert.assertEquals(50f, root_child2.GetLayoutY());
//            Assert.assertEquals(50f, root_child2.GetLayoutWidth());
//            Assert.assertEquals(50f, root_child2.GetLayoutHeight());
//        }
//
//        @Test
//        public void Test_flex_basis_overrides_main_size()
//        {
//            YogaConfig config = new YogaConfig();
//
//            YogaNode root = new YogaNode(config);
//            root.SetWidth(YogaValue.Pt(100;
//            root.SetHeight(YogaValue.Pt(100;
//
//            YogaNode root_child0 = new YogaNode(config);
//            root_child0.SetFlexGrow(1;
//            root_child0.SetFlexGrow(YogaValue.Pt(50;
//            root_child0.SetHeight(YogaValue.Pt(20;
//            root.Insert(0, root_child0);
//
//            YogaNode root_child1 = new YogaNode(config);
//            root_child1.SetFlexGrow(1;
//            root_child1.SetHeight(YogaValue.Pt(10;
//            root.Insert(1, root_child1);
//
//            YogaNode root_child2 = new YogaNode(config);
//            root_child2.SetFlexGrow(1;
//            root_child2.SetHeight(YogaValue.Pt(10;
//            root.Insert(2, root_child2);
//            root.SetStyleDirection(YogaDirection.LeftToRight;
//            root.CalculateLayout();
//
//            Assert.assertEquals(0f, root.GetLayoutX());
//            Assert.assertEquals(0f, root.GetLayoutY());
//            Assert.assertEquals(100f, root.GetLayoutWidth());
//            Assert.assertEquals(100f, root.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child0.GetLayoutX());
//            Assert.assertEquals(0f, root_child0.GetLayoutY());
//            Assert.assertEquals(100f, root_child0.GetLayoutWidth());
//            Assert.assertEquals(60f, root_child0.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child1.GetLayoutX());
//            Assert.assertEquals(60f, root_child1.GetLayoutY());
//            Assert.assertEquals(100f, root_child1.GetLayoutWidth());
//            Assert.assertEquals(20f, root_child1.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child2.GetLayoutX());
//            Assert.assertEquals(80f, root_child2.GetLayoutY());
//            Assert.assertEquals(100f, root_child2.GetLayoutWidth());
//            Assert.assertEquals(20f, root_child2.GetLayoutHeight());
//
//            root.SetStyleDirection(YogaDirection.RightToLeft;
//            root.CalculateLayout();
//
//            Assert.assertEquals(0f, root.GetLayoutX());
//            Assert.assertEquals(0f, root.GetLayoutY());
//            Assert.assertEquals(100f, root.GetLayoutWidth());
//            Assert.assertEquals(100f, root.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child0.GetLayoutX());
//            Assert.assertEquals(0f, root_child0.GetLayoutY());
//            Assert.assertEquals(100f, root_child0.GetLayoutWidth());
//            Assert.assertEquals(60f, root_child0.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child1.GetLayoutX());
//            Assert.assertEquals(60f, root_child1.GetLayoutY());
//            Assert.assertEquals(100f, root_child1.GetLayoutWidth());
//            Assert.assertEquals(20f, root_child1.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child2.GetLayoutX());
//            Assert.assertEquals(80f, root_child2.GetLayoutY());
//            Assert.assertEquals(100f, root_child2.GetLayoutWidth());
//            Assert.assertEquals(20f, root_child2.GetLayoutHeight());
//        }
//
//        @Test
//        public void Test_flex_grow_shrink_at_most()
//        {
//            YogaConfig config = new YogaConfig();
//
//            YogaNode root = new YogaNode(config);
//            root.SetWidth(YogaValue.Pt(100;
//            root.SetHeight(YogaValue.Pt(100;
//
//            YogaNode root_child0 = new YogaNode(config);
//            root.Insert(0, root_child0);
//
//            YogaNode root_child0_child0 = new YogaNode(config);
//            root_child0_child0.SetFlexGrow(1;
//            root_child0_child0.FlexShrink = 1;
//            root_child0.Insert(0, root_child0_child0);
//            root.SetStyleDirection(YogaDirection.LeftToRight;
//            root.CalculateLayout();
//
//            Assert.assertEquals(0f, root.GetLayoutX());
//            Assert.assertEquals(0f, root.GetLayoutY());
//            Assert.assertEquals(100f, root.GetLayoutWidth());
//            Assert.assertEquals(100f, root.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child0.GetLayoutX());
//            Assert.assertEquals(0f, root_child0.GetLayoutY());
//            Assert.assertEquals(100f, root_child0.GetLayoutWidth());
//            Assert.assertEquals(0f, root_child0.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child0_child0.GetLayoutX());
//            Assert.assertEquals(0f, root_child0_child0.GetLayoutY());
//            Assert.assertEquals(100f, root_child0_child0.GetLayoutWidth());
//            Assert.assertEquals(0f, root_child0_child0.GetLayoutHeight());
//
//            root.SetStyleDirection(YogaDirection.RightToLeft;
//            root.CalculateLayout();
//
//            Assert.assertEquals(0f, root.GetLayoutX());
//            Assert.assertEquals(0f, root.GetLayoutY());
//            Assert.assertEquals(100f, root.GetLayoutWidth());
//            Assert.assertEquals(100f, root.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child0.GetLayoutX());
//            Assert.assertEquals(0f, root_child0.GetLayoutY());
//            Assert.assertEquals(100f, root_child0.GetLayoutWidth());
//            Assert.assertEquals(0f, root_child0.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child0_child0.GetLayoutX());
//            Assert.assertEquals(0f, root_child0_child0.GetLayoutY());
//            Assert.assertEquals(100f, root_child0_child0.GetLayoutWidth());
//            Assert.assertEquals(0f, root_child0_child0.GetLayoutHeight());
//        }
//
//        @Test
//        public void Test_flex_grow_less_than_factor_one()
//        {
//            YogaConfig config = new YogaConfig();
//
//            YogaNode root = new YogaNode(config);
//            root.SetWidth(YogaValue.Pt(200;
//            root.SetHeight(YogaValue.Pt(500;
//
//            YogaNode root_child0 = new YogaNode(config);
//            root_child0.SetFlexGrow(0.2f;
//            root_child0.SetFlexGrow(YogaValue.Pt(40;
//            root.Insert(0, root_child0);
//
//            YogaNode root_child1 = new YogaNode(config);
//            root_child1.SetFlexGrow(0.2f;
//            root.Insert(1, root_child1);
//
//            YogaNode root_child2 = new YogaNode(config);
//            root_child2.SetFlexGrow(0.4f;
//            root.Insert(2, root_child2);
//            root.SetStyleDirection(YogaDirection.LeftToRight;
//            root.CalculateLayout();
//
//            Assert.assertEquals(0f, root.GetLayoutX());
//            Assert.assertEquals(0f, root.GetLayoutY());
//            Assert.assertEquals(200f, root.GetLayoutWidth());
//            Assert.assertEquals(500f, root.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child0.GetLayoutX());
//            Assert.assertEquals(0f, root_child0.GetLayoutY());
//            Assert.assertEquals(200f, root_child0.GetLayoutWidth());
//            Assert.assertEquals(132f, root_child0.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child1.GetLayoutX());
//            Assert.assertEquals(132f, root_child1.GetLayoutY());
//            Assert.assertEquals(200f, root_child1.GetLayoutWidth());
//            Assert.assertEquals(92f, root_child1.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child2.GetLayoutX());
//            Assert.assertEquals(224f, root_child2.GetLayoutY());
//            Assert.assertEquals(200f, root_child2.GetLayoutWidth());
//            Assert.assertEquals(184f, root_child2.GetLayoutHeight());
//
//            root.SetStyleDirection(YogaDirection.RightToLeft;
//            root.CalculateLayout();
//
//            Assert.assertEquals(0f, root.GetLayoutX());
//            Assert.assertEquals(0f, root.GetLayoutY());
//            Assert.assertEquals(200f, root.GetLayoutWidth());
//            Assert.assertEquals(500f, root.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child0.GetLayoutX());
//            Assert.assertEquals(0f, root_child0.GetLayoutY());
//            Assert.assertEquals(200f, root_child0.GetLayoutWidth());
//            Assert.assertEquals(132f, root_child0.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child1.GetLayoutX());
//            Assert.assertEquals(132f, root_child1.GetLayoutY());
//            Assert.assertEquals(200f, root_child1.GetLayoutWidth());
//            Assert.assertEquals(92f, root_child1.GetLayoutHeight());
//
//            Assert.assertEquals(0f, root_child2.GetLayoutX());
//            Assert.assertEquals(224f, root_child2.GetLayoutY());
//            Assert.assertEquals(200f, root_child2.GetLayoutWidth());
//            Assert.assertEquals(184f, root_child2.GetLayoutHeight());
//        }
//
//        @Test
//        public void Test_flex_shrink_flex_grow_row()
//        {
//            YogaConfig config = new YogaConfig();
//            YogaNode root = new YogaNode(config);
//            root.FlexDirection = YogaFlexDirection.Row;
//            root.SetWidth(YogaValue.Pt(500;
//            root.SetHeight(YogaValue.Pt(500;
//
//            YogaNode root_child0 = new YogaNode(config);
//            root_child0.FlexShrink = 1;
//            root_child0.SetWidth(YogaValue.Pt(500;
//            root_child0.SetHeight(YogaValue.Pt(100;
//            root.Insert(0, root_child0);
//
//            YogaNode root_child1 = new YogaNode(config);
//            root_child1.FlexShrink = 1;
//            root_child1.SetWidth(YogaValue.Pt(500;
//            root_child1.SetHeight(YogaValue.Pt(100;
//            root.Insert(1, root_child1);
//
//            root.SetStyleDirection(YogaDirection.LeftToRight;
//            root.CalculateLayout();
//
//            Assert.assertEquals(0f, root.GetLayoutX());
//            Assert.assertEquals(0f, root.GetLayoutY());
//            Assert.assertEquals(500f, root.GetLayoutWidth());
//            Assert.assertEquals(500f, root.GetLayoutHeight());
//            Assert.assertEquals(0f, root_child0.GetLayoutX());
//            Assert.assertEquals(0f, root_child0.GetLayoutY());
//            Assert.assertEquals(250f, root_child0.GetLayoutWidth());
//            Assert.assertEquals(100f, root_child0.GetLayoutHeight());
//            Assert.assertEquals(250f, root_child1.GetLayoutX());
//            Assert.assertEquals(0f, root_child1.GetLayoutY());
//            Assert.assertEquals(250f, root_child1.GetLayoutWidth());
//            Assert.assertEquals(100f, root_child1.GetLayoutHeight());
//
//            root.SetStyleDirection(YogaDirection.RightToLeft;
//            root.CalculateLayout();
//
//            Assert.assertEquals(0f, root.GetLayoutX());
//            Assert.assertEquals(0f, root.GetLayoutY());
//            Assert.assertEquals(500f, root.GetLayoutWidth());
//            Assert.assertEquals(500f, root.GetLayoutHeight());
//            Assert.assertEquals(250f, root_child0.GetLayoutX());
//            Assert.assertEquals(0f, root_child0.GetLayoutY());
//            Assert.assertEquals(250f, root_child0.GetLayoutWidth());
//            Assert.assertEquals(100f, root_child0.GetLayoutHeight());
//            Assert.assertEquals(0f, root_child1.GetLayoutX());
//            Assert.assertEquals(0f, root_child1.GetLayoutY());
//            Assert.assertEquals(250f, root_child1.GetLayoutWidth());
//            Assert.assertEquals(100f, root_child1.GetLayoutHeight());
//        }
//
//        @Test
//        public void Test_flex_shrink_flex_grow_child_flex_shrink_other_child()
//        {
//            YogaConfig config = new YogaConfig();
//            YogaNode root = new YogaNode(config);
//            root.FlexDirection = YogaFlexDirection.Row;
//            root.SetWidth(YogaValue.Pt(500;
//            root.SetHeight(YogaValue.Pt(500;
//
//            YogaNode root_child0 = new YogaNode(config);
//            root_child0.FlexShrink = 1;
//            root_child0.SetWidth(YogaValue.Pt(500;
//            root_child0.SetHeight(YogaValue.Pt(100;
//            root.Insert(0, root_child0);
//
//            YogaNode root_child1 = new YogaNode(config);
//            root_child1.SetFlexGrow(1;
//            root_child1.FlexShrink = 1;
//            root_child1.SetWidth(YogaValue.Pt(500;
//            root_child1.SetHeight(YogaValue.Pt(100;
//            root.Insert(1, root_child1);
//
//            root.SetStyleDirection(YogaDirection.LeftToRight;
//            root.CalculateLayout();
//
//            Assert.assertEquals(0f, root.GetLayoutX());
//            Assert.assertEquals(0f, root.GetLayoutY());
//            Assert.assertEquals(500f, root.GetLayoutWidth());
//            Assert.assertEquals(500f, root.GetLayoutHeight());
//            Assert.assertEquals(0f, root_child0.GetLayoutX());
//            Assert.assertEquals(0f, root_child0.GetLayoutY());
//            Assert.assertEquals(250f, root_child0.GetLayoutWidth());
//            Assert.assertEquals(100f, root_child0.GetLayoutHeight());
//            Assert.assertEquals(250f, root_child1.GetLayoutX());
//            Assert.assertEquals(0f, root_child1.GetLayoutY());
//            Assert.assertEquals(250f, root_child1.GetLayoutWidth());
//            Assert.assertEquals(100f, root_child1.GetLayoutHeight());
//
//            root.SetStyleDirection(YogaDirection.RightToLeft;
//            root.CalculateLayout();
//
//            Assert.assertEquals(0f, root.GetLayoutX());
//            Assert.assertEquals(0f, root.GetLayoutY());
//            Assert.assertEquals(500f, root.GetLayoutWidth());
//            Assert.assertEquals(500f, root.GetLayoutHeight());
//            Assert.assertEquals(250f, root_child0.GetLayoutX());
//            Assert.assertEquals(0f, root_child0.GetLayoutY());
//            Assert.assertEquals(250f, root_child0.GetLayoutWidth());
//            Assert.assertEquals(100f, root_child0.GetLayoutHeight());
//            Assert.assertEquals(0f, root_child1.GetLayoutX());
//            Assert.assertEquals(0f, root_child1.GetLayoutY());
//            Assert.assertEquals(250f, root_child1.GetLayoutWidth());
//            Assert.assertEquals(100f, root_child1.GetLayoutHeight());
//        }
//
//    */
//}
