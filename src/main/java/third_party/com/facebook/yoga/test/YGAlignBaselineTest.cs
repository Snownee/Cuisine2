/*
 *  Copyright (c) 2014-present, Facebook, Inc.
 *
 *  This source code is licensed under the MIT license found in the LICENSE
 *  file in the root directory of this source tree.
 *
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using NUnit.Framework;

namespace Marius.Yoga
{
    [TestFixture]
    public class YGAlignBaselineTest
    {
        private static float _baselineFunc(YogaNode node, float? width, float? height)
        {
            var result = height / 2;
            return result ?? 0;
        }

        private static YogaSize _measure1(YogaNode node, float? width, YogaMeasureMode widthMode, float? height, YogaMeasureMode heightMode)
        {
            return new YogaSize
            {
                Width = 42,
                Height = 50,
            };
        }

        private static YogaSize _measure2(YogaNode node, float? width, YogaMeasureMode widthMode, float? height, YogaMeasureMode heightMode)
        {
            return new YogaSize
            {
                Width = 279,
                Height = 126,
            };
        }

        // Test case for bug in T32999822
        [Test]
        public void Test_align_baseline_parent_ht_not_specified()
        {
            var config = new YogaConfig();

            var root = new YogaNode(config);
            root.Style.FlexDirection = YogaFlexDirection.Row;
            root.Style.AlignContent = YogaAlign.Stretch;
            root.Style.AlignItems = YogaAlign.Baseline;
            root.Width = 340;
            root.MaxHeight = 170;
            root.MinHeight = 0;

            var root_child0 = new YogaNode(config);
            root_child0.Style.FlexGrow = 0;
            root_child0.Style.FlexShrink = 1;
            root_child0.SetMeasureFunction(_measure1);
            root.Insert(0, root_child0);

            var root_child1 = new YogaNode(config);
            root_child1.Style.FlexGrow = 0;
            root_child1.Style.FlexShrink = 1;
            root_child1.SetMeasureFunction(_measure2);
            root.Insert(1, root_child1);

            root.CalculateLayout(null, null, YogaDirection.LeftToRight);

            Assert.AreEqual(0, root.LayoutX);
            Assert.AreEqual(0, root.LayoutY);
            Assert.AreEqual(340, root.LayoutWidth);
            Assert.AreEqual(126, root.LayoutHeight);

            Assert.AreEqual(0, root_child0.LayoutX);
            Assert.AreEqual(42, root_child0.LayoutWidth);
            Assert.AreEqual(50, root_child0.LayoutHeight);
            Assert.AreEqual(76, root_child0.LayoutY);

            Assert.AreEqual(42, root_child1.LayoutX);
            Assert.AreEqual(0, root_child1.LayoutY);
            Assert.AreEqual(279, root_child1.LayoutWidth);
            Assert.AreEqual(126, root_child1.LayoutHeight);
        }


        [Test]
        public void Test_align_baseline_with_no_parent_ht()
        {
            var config = new YogaConfig();

            var root = new YogaNode(config);
            root.Style.FlexDirection = YogaFlexDirection.Row;
            root.Style.AlignItems = YogaAlign.Baseline;
            root.Width = 150;

            var root_child0 = new YogaNode(config);
            root_child0.Width = 50;
            root_child0.Height = 50;
            root.Insert(0, root_child0);

            var root_child1 = new YogaNode(config);
            root_child1.Width = 50;
            root_child1.Height = 40;
            root_child1.SetBaselineFunction(_baselineFunc);
            root.Insert(1, root_child1);

            root.CalculateLayout(null, null, YogaDirection.LeftToRight);

            Assert.AreEqual(0, root.LayoutX);
            Assert.AreEqual(0, root.LayoutY);
            Assert.AreEqual(150, root.LayoutWidth);
            Assert.AreEqual(70, root.LayoutHeight);

            Assert.AreEqual(0, root_child0.LayoutX);
            Assert.AreEqual(0, root_child0.LayoutY);
            Assert.AreEqual(50, root_child0.LayoutWidth);
            Assert.AreEqual(50, root_child0.LayoutHeight);

            Assert.AreEqual(50, root_child1.LayoutX);
            Assert.AreEqual(30, root_child1.LayoutY);
            Assert.AreEqual(50, root_child1.LayoutWidth);
            Assert.AreEqual(40, root_child1.LayoutHeight);
        }

        [Test]
        public void Test_align_baseline_with_no_baseline_func_and_no_parent_ht()
        {
            var config = new YogaConfig();

            var root = new YogaNode(config);
            root.Style.FlexDirection = YogaFlexDirection.Row;
            root.Style.AlignItems = YogaAlign.Baseline;
            root.Width = 150;

            var root_child0 = new YogaNode(config);
            root_child0.Width = 50;
            root_child0.Height = 80;
            root.Insert(0, root_child0);

            var root_child1 = new YogaNode(config);
            root_child1.Width = 50;
            root_child1.Height = 50;
            root.Insert(1, root_child1);

            root.CalculateLayout(null, null, YogaDirection.LeftToRight);

            Assert.AreEqual(0, root.LayoutX);
            Assert.AreEqual(0, root.LayoutY);
            Assert.AreEqual(150, root.LayoutWidth);
            Assert.AreEqual(80, root.LayoutHeight);

            Assert.AreEqual(0, root_child0.LayoutX);
            Assert.AreEqual(0, root_child0.LayoutY);
            Assert.AreEqual(50, root_child0.LayoutWidth);
            Assert.AreEqual(80, root_child0.LayoutHeight);

            Assert.AreEqual(50, root_child1.LayoutX);
            Assert.AreEqual(30, root_child1.LayoutY);
            Assert.AreEqual(50, root_child1.LayoutWidth);
            Assert.AreEqual(50, root_child1.LayoutHeight);
        }

        [Test]
        public void Test_align_baseline_parent_using_child_in_column_as_reference()
        {
            var config = new YogaConfig();
            var root = createYGNode(config, YogaFlexDirection.Row, 1000, 1000, true);

            var root_child0 = createYGNode(config, YogaFlexDirection.Column, 500, 600, false);
            root.Insert(0, root_child0);

            var root_child1 = createYGNode(config, YogaFlexDirection.Column, 500, 800, false);
            root.Insert(1, root_child1);

            var root_child1_child0 = createYGNode(config, YogaFlexDirection.Column, 500, 300, false);
            root_child1.Insert(0, root_child1_child0);

            var root_child1_child1 = createYGNode(config, YogaFlexDirection.Column, 500, 400, false);
            root_child1_child1.SetBaselineFunction((_, width, height) =>
            {
                return (height / 2) ?? 0;
            });
            root_child1_child1.IsReferenceBaseline = true;
            root_child1.Insert(1, root_child1_child1);

            root.CalculateLayout();

            Assert.AreEqual(0f, root_child0.LayoutX);
            Assert.AreEqual(0f, root_child0.LayoutY);

            Assert.AreEqual(500f, root_child1.LayoutX);
            Assert.AreEqual(100f, root_child1.LayoutY);

            Assert.AreEqual(0f, root_child1_child0.LayoutX);
            Assert.AreEqual(0f, root_child1_child0.LayoutY);

            Assert.AreEqual(0f, root_child1_child1.LayoutX);
            Assert.AreEqual(300f, root_child1_child1.LayoutY);
        }

        [Test]
        public void Test_align_baseline_parent_using_child_in_row_as_reference()
        {
            var config = new YogaConfig();

            var root = createYGNode(config, YogaFlexDirection.Row, 1000, 1000, true);

            var root_child0 = createYGNode(config, YogaFlexDirection.Column, 500, 600, false);
            root.Insert(0, root_child0);

            var root_child1 = createYGNode(config, YogaFlexDirection.Row, 500, 800, true);
            root.Insert(1, root_child1);

            var root_child1_child0 = createYGNode(config, YogaFlexDirection.Row, 500, 500, false);
            root_child1.Insert(0, root_child1_child0);

            var root_child1_child1 = createYGNode(config, YogaFlexDirection.Row, 500, 400, false);
            root_child1_child1.SetBaselineFunction((_, width, height) =>
            {
                return (height / 2) ?? 0;
            });
            root_child1_child1.IsReferenceBaseline = true;
            root_child1.Insert(1, root_child1_child1);

            root.StyleDirection = YogaDirection.LeftToRight;

            root.CalculateLayout();

            Assert.AreEqual(0f, root_child0.LayoutX);
            Assert.AreEqual(0f, root_child0.LayoutY);

            Assert.AreEqual(500f, root_child1.LayoutX);
            Assert.AreEqual(100f, root_child1.LayoutY);

            Assert.AreEqual(0f, root_child1_child0.LayoutX);
            Assert.AreEqual(0f, root_child1_child0.LayoutY);

            Assert.AreEqual(500f, root_child1_child1.LayoutX);
            Assert.AreEqual(300f, root_child1_child1.LayoutY);
        }

        private YogaNode createYGNode(YogaConfig config, YogaFlexDirection flexDirection, int width, int height, bool alignBaseline)
        {
            var node = new YogaNode(config);
            node.FlexDirection = flexDirection;
            node.Width = width;
            node.Height = height;
            if (alignBaseline)
            {
                node.AlignItems = YogaAlign.Baseline;
            }
            return node;
        }
    }
}
