/**
 * Copyright (c) 2014-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Marius.Yoga
{
    public static class MeasureOutput
    {
        public static YogaSize Make(float width, float height)
        {
            return new YogaSize { Width = width, Height = height };
        }
    }
}
