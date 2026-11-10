package net.n2oapp.framework.access.metadata.validation;

import net.n2oapp.framework.access.metadata.BehaviorEnum;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oAbstractCell;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Валидатор security атрибутов для ячеек.
 * Проверяет, что если у ячейки указаны security атрибуты из схемы security-1.0,
 * то должен быть указан behavior=disable или настройка n2o.access.behavior должна иметь значение disable.
 */
@Component
public class CellSecurityValidator implements SourceValidator<N2oAbstractCell>, SourceClassAware {

    private static final String SECURITY_NAMESPACE_URI = "http://n2oapp.net/framework/config/schema/security-1.0";
    private static final String BEHAVIOR_ATTRIBUTE = "behavior";
    private static final String BEHAVIOR_VALID_IDS = Arrays.stream(BehaviorEnum.values())
            .map(BehaviorEnum::getId)
            .collect(Collectors.joining(" | ", "[", "]"));

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oAbstractCell.class;
    }

    @Override
    public void validate(N2oAbstractCell source, SourceProcessor p) {
        if (source.getExtAttributes() == null) {
            return;
        }

        N2oNamespace securityNamespace = findSecurityNamespace(source.getExtAttributes());
        if (securityNamespace == null) {
            return;
        }

        Map<String, String> securityAttributes = source.getExtAttributes().get(securityNamespace);
        if (securityAttributes == null || securityAttributes.isEmpty()) {
            return;
        }

        BehaviorEnum behavior = resolveBehavior(securityAttributes, p);
        if (behavior != BehaviorEnum.DISABLE) {
            throw new N2oMetadataValidationException(
                    "Для ячейки с security атрибутами необходимо указать sec:behavior=\"disable\" " +
                            "или поведение disable должно быть задано по умолчанию настройкой n2o.access.behavior");
        }
    }

    private N2oNamespace findSecurityNamespace(Map<N2oNamespace, Map<String, String>> extAttributes) {
        for (N2oNamespace namespace : extAttributes.keySet()) {
            if (SECURITY_NAMESPACE_URI.equals(namespace.getUri())) {
                return namespace;
            }
        }
        return null;
    }

    private BehaviorEnum resolveBehavior(Map<String, String> securityAttributes, SourceProcessor p) {
        if (securityAttributes.containsKey(BEHAVIOR_ATTRIBUTE)) {
            String behaviorValue = securityAttributes.get(BEHAVIOR_ATTRIBUTE);
            try {
                return BehaviorEnum.valueOf(behaviorValue.toUpperCase());
            } catch (IllegalArgumentException | NullPointerException e) {
                throw new N2oMetadataValidationException(
                        String.format("Недопустимое значение атрибута 'sec:behavior=\"%s\"'. Допустимые значения: %s",
                                behaviorValue, BEHAVIOR_VALID_IDS));
            }
        }
        return p.resolve(property("n2o.access.behavior"), BehaviorEnum.class);
    }
}