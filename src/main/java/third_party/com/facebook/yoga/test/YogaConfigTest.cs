/**
 * Copyright (c) 2014-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

using NUnit.Framework;
using System;

/**
 * Tests for {@link YogaConfig}.
 */
namespace Marius.Yoga
{
    [TestFixture]
    public class YogaConfigTest
    {
        [Test]
        public void TestUseWebDefaults()
        {
            YogaNode node0 = new YogaNode(new YogaConfig{UseWebDefaults = true});
            Assert.AreEqual(YogaFlexDirection.Row, node0.FlexDirection);

            node0.Reset();
            Assert.AreEqual(YogaFlexDirection.Row, node0.FlexDirection);

            YogaConfig config = new YogaConfig();
            config.UseWebDefaults = true;
            YogaNode node1 = new YogaNode(config);
            Assert.AreEqual(YogaFlexDirection.Row, node1.FlexDirection);

            node1.Reset();
            Assert.AreEqual(YogaFlexDirection.Row, node1.FlexDirection);
        }

        [Test]
        public void TestDefaultConfig()
        {
            YogaNode node0 = new YogaNode();
            Assert.AreEqual(YogaFlexDirection.Column, node0.FlexDirection);

            YogaNode node1 = new YogaNode(new YogaConfig());
            Assert.AreEqual(YogaFlexDirection.Column, node1.FlexDirection);
        }

        [Test]
        public void TestCopyConstructor()
        {
            YogaNode srcNode = new YogaNode(new YogaConfig{UseWebDefaults = true});
            YogaNode node0 = new YogaNode(srcNode);
            Assert.AreEqual(YogaFlexDirection.Row, node0.FlexDirection);

            node0.FlexDirection = YogaFlexDirection.Column;
            Assert.AreEqual(YogaFlexDirection.Column, node0.FlexDirection);

            node0.Reset();
            Assert.AreEqual(YogaFlexDirection.Row, node0.FlexDirection);

            YogaNode node1 = new YogaNode(srcNode)
            {
                FlexDirection = YogaFlexDirection.Column
            };
            Assert.AreEqual(YogaFlexDirection.Column, node1.FlexDirection);

            node1.Reset();
            Assert.AreEqual(YogaFlexDirection.Row, node1.FlexDirection);
        }
    }
}
