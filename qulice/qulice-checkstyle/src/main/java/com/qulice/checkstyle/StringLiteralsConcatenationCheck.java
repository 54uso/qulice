/**
 * Copyright (c) 2011, Qulice.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the Qulice.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.qulice.checkstyle;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.ArrayList;
import java.util.List;

/**
 * Checks not using concatenation of string literals in any form. I.e. the
 * following is prohibited: <br/>
 * String a = "done in " + time + " seconds"; <br/>
 * System.out.println("File not found: " + file); <br/>
 * x += "done";
 * @author Dzmitry Petrushenka (dpetruha@gmail.com)
 * @version $Id$
 */
public final class StringLiteralsConcatenationCheck extends Check {

    /**
     * Error message.
     */
    private static final String ERR_MSG = "Concatenation of string literals";

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] getDefaultTokens() {
        return new int[] {TokenTypes.OBJBLOCK};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitToken(final DetailAST ast) {
        for (DetailAST plusRootAst : this.findChildASTsOfType(ast,
            TokenTypes.PLUS, TokenTypes.PLUS_ASSIGN)) {
            if (plusRootAst.getChildCount(TokenTypes.STRING_LITERAL) > 0) {
                this.log(plusRootAst, this.ERR_MSG);
            }
        }
    }

    /**
     * Recursively traverse the <code>tree</code> and return all ASTs subtrees
     * matching any type from <code>types</code>.
     * @param tree AST to traverse.
     * @param types Token types to match against.
     * @return All ASTs subtrees with token types matching any from
     * <code>types</code>.
     * @see TokenTypes
     */
    private List<DetailAST> findChildASTsOfType(final DetailAST tree,
        final int... types) {
        final List<DetailAST> children = new ArrayList<DetailAST>();
        DetailAST child = tree.getFirstChild();
        while (child != null) {
            if (this.isOfType(child, types)) {
                children.add(child);
            } else {
                children.addAll(this.findChildASTsOfType(child, types));
            }
            child = child.getNextSibling();
        }
        return children;
    }

    /**
     * Checks if this <code>ast</code> is of any type from <code>types</code>.
     * @param ast AST to check.
     * @param types Token types to match against.
     * @return True if of type, false otherwise.
     * @see TokenTypes
     */
    private boolean isOfType(final DetailAST ast, final int[] types) {
        boolean isOfType = false;
        for (int type : types) {
            if (ast.getType() == type) {
                isOfType = true;
                break;
            }
        }
        return isOfType;
    }
}