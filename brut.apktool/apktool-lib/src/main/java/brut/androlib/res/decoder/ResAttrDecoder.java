/*
 *  Copyright (C) 2010 Ryszard Wiśniewski <brut.alll@gmail.com>
 *  Copyright (C) 2010 Connor Tumbleson <connor.tumbleson@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package brut.androlib.res.decoder;

import brut.androlib.AndrolibException;
import brut.androlib.err.UndefinedResObjectException;
import brut.androlib.res.data.ResResSpec;
import brut.androlib.res.data.ResTable;
import brut.androlib.res.data.value.ResAttr;
import brut.androlib.res.data.value.ResScalarValue;

public class ResAttrDecoder {
    public String decode(int type, int value, String rawValue, int attrResId)
            throws AndrolibException {
        ResScalarValue resValue = mResTable.getCurrentResPackage().getValueFactory()
                .factory(type, value, rawValue);

        String decoded = null;
        if (attrResId > 0) {
            try {
                ResAttr attr = (ResAttr) getResTable().getResSpec(attrResId).getDefaultResource().getValue();

                decoded = attr.convertToResXmlFormat(resValue);
            } catch (UndefinedResObjectException | ClassCastException ex) {
                // ignored
            }
        }

        return decoded != null ? decoded : resValue.encodeAsResXmlAttr();
    }

    public String decodeManifestAttr(int attrResId)
        throws AndrolibException {

        if (attrResId != 0) {
            ResResSpec resResSpec = getResTable().getResSpec(attrResId);

            if (resResSpec != null) {
                return resResSpec.getName();
            }
        }

        return null;
    }

    public void setResTable(ResTable resTable) {
        mResTable = resTable;
    }

    public ResTable getResTable() throws AndrolibException {
        if (mResTable == null) {
            throw new AndrolibException("Current resTable not set.");
        }
        return mResTable;
    }

    private ResTable mResTable;
}
