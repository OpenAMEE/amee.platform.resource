package com.amee.platform.resource.dataitem.v_3_0;

import com.amee.base.domain.Since;
import com.amee.base.resource.*;
import com.amee.base.transaction.AMEETransaction;
import com.amee.base.validation.ValidationException;
import com.amee.domain.data.DataCategory;
import com.amee.domain.item.data.DataItem;
import com.amee.platform.resource.dataitem.DataItemResource;
import com.amee.service.auth.ResourceAuthorizationService;
import com.amee.service.data.DataService;
import com.amee.service.item.DataItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope("prototype")
@Since("3.0.0")
public class DataItemFormAcceptor_3_0_0 implements DataItemResource.FormAcceptor {

    @Autowired
    private DataService dataService;

    @Autowired
    private DataItemService dataItemService;

    @Autowired
    private ResourceAuthorizationService resourceAuthorizationService;

    @Autowired
    private ResourceBeanFinder resourceBeanFinder;

    @Override
    @AMEETransaction
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Object handle(RequestWrapper requestWrapper) throws ValidationException {
        // Get DataCategory identifier.
        String dataCategoryIdentifier = requestWrapper.getAttributes().get("categoryIdentifier");
        if (dataCategoryIdentifier != null) {
            // Get DataCategory.
            DataCategory dataCategory = dataService.getDataCategoryByIdentifier(dataCategoryIdentifier);
            if (dataCategory != null) {
                // Get DataItem identifier.
                String dataItemIdentifier = requestWrapper.getAttributes().get("itemIdentifier");
                if (dataItemIdentifier != null) {
                    // Get DataItem.
                    DataItem dataItem = dataItemService.getDataItemByUid(dataCategory, dataItemIdentifier);
                    if (dataItem != null) {
                        // Authorized?
                        resourceAuthorizationService.ensureAuthorizedForModify(
                                requestWrapper.getAttributes().get("activeUserUid"), dataItem);
                        // Handle the DataItem update (entity updated via validation binding).
                        DataItemResource.DataItemValidator validator = getValidator(requestWrapper);
                        validator.setObject(dataItem);
                        if (validator.isValid(requestWrapper.getFormParameters())) {
                            return ResponseHelper.getOK(requestWrapper);
                        } else {
                            throw new ValidationException(validator.getValidationResult());
                        }
                    } else {
                        throw new NotFoundException();
                    }
                } else {
                    throw new MissingAttributeException("itemIdentifier");
                }
            } else {
                throw new NotFoundException();
            }
        } else {
            throw new MissingAttributeException("categoryIdentifier");
        }
    }

    protected DataItemResource.DataItemValidator getValidator(RequestWrapper requestWrapper) {
        return (DataItemResource.DataItemValidator)
                resourceBeanFinder.getValidationHelper(
                        DataItemResource.DataItemValidator.class, requestWrapper);
    }
}