package com.amee.platform.resource.returnvaluedefinition.v_3_1;

import com.amee.base.domain.Since;
import com.amee.platform.resource.returnvaluedefinition.v_3_2.ReturnValueDefinitionJSONRenderer_3_2_0;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
@Since("3.1.0")
public class ReturnValueDefinitionJSONRenderer_3_1_0 extends ReturnValueDefinitionJSONRenderer_3_2_0 {

    @Override
    public void addAudit() {
        // Not in 3.1.
    }
}
