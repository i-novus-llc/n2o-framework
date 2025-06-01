package net.n2oapp.framework.ui.controller;

import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesModeEnum;
import net.n2oapp.framework.api.rest.*;
import net.n2oapp.framework.api.ui.*;
import net.n2oapp.framework.config.factory.BaseMetadataFactory;
import net.n2oapp.framework.ui.controller.action.SetController;
import net.n2oapp.framework.ui.controller.action.ValidationController;
import net.n2oapp.framework.ui.controller.query.GetController;

import java.util.Map;
import java.util.function.BiPredicate;


public class N2oControllerFactory extends BaseMetadataFactory implements ControllerFactory {

    public N2oControllerFactory(Map<String, ?> beans) {
        super(beans);
    }

    public N2oControllerFactory() {
    }

    @Override
    public GetDataResponse execute(QueryRequestInfo request, QueryResponseInfo response) {
        DefaultValuesModeEnum formModel = request.getMode() != null ? request.getMode() : DefaultValuesModeEnum.QUERY;
        BiPredicate<ControllerTypeAware, DefaultValuesModeEnum> predicate = (controller, uploadType) -> formModel.name().equals(controller.getControllerType().name());
        GetController controller = (GetController) produce(predicate, formModel);
        return controller.execute(request, response);
    }

    @Override
    public SetDataResponse execute(ActionRequestInfo request, ActionResponseInfo response) {
        BiPredicate<ControllerTypeAware, ?> predicate = (controller, something) -> ControllerTypeEnum.OPERATION == controller.getControllerType();
        SetController controller = (SetController) produce(predicate, null);
        return controller.execute(request, response);
    }

    @Override
    public ValidationDataResponse execute(ValidationRequestInfo request, ValidationResponseInfo response) {
        BiPredicate<ControllerTypeAware, ?> predicate = (controller, something) -> ControllerTypeEnum.VALIDATION == controller.getControllerType();
        ValidationController controller = (ValidationController) produce(predicate, null);
        return controller.execute(request, response);
    }
}
