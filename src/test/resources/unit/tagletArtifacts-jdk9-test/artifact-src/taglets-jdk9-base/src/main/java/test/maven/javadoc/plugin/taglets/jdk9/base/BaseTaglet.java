/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package test.maven.javadoc.plugin.taglets.jdk9.base;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.sun.source.doctree.DocTree;
import com.sun.source.doctree.TextTree;
import com.sun.source.doctree.UnknownBlockTagTree;
import com.sun.source.doctree.UnknownInlineTagTree;
import com.sun.source.util.SimpleDocTreeVisitor;

import jdk.javadoc.doclet.Taglet;

public abstract class BaseTaglet implements Taglet {
    private static final Set<Location> ALLOWED_LOCATIONS = Collections
            .unmodifiableSet(EnumSet.allOf(Location.class));

    private final boolean inline;
    private final String name;

    public BaseTaglet(String name, boolean inline) {
        this.name = name;
        this.inline = inline;
    }

    @Override
    public Set<Location> getAllowedLocations() {
        return ALLOWED_LOCATIONS;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isInlineTag() {
        return inline;
    }

    protected String getText(DocTree dt) {
        return new SimpleDocTreeVisitor<String, Void>() {
            @Override
            public String visitText(TextTree node, Void p) {
                return node.getBody();
            }

            @Override
            public String visitUnknownBlockTag(UnknownBlockTagTree node, Void p) {
                for (DocTree dt : node.getContent())
                    return dt.accept(this, null);
                return "";
            }

            @Override
            public String visitUnknownInlineTag(UnknownInlineTagTree node, Void p) {
                for (DocTree dt : node.getContent())
                    return dt.accept(this, null);
                return "";
            }

            @Override
            protected String defaultAction(DocTree node, Void p) {
                return "";
            }

        }.visit(dt, null);
    }
}
