import React, { useContext, useEffect, useMemo, useRef } from 'react'
import PropTypes from 'prop-types'
import { useSelector } from 'react-redux'
import isBoolean from 'lodash/isBoolean'
import get from 'lodash/get'
import isNil from 'lodash/isNil'
import isEmpty from 'lodash/isEmpty'

import { makeFieldByName, messageSelector } from '../../../../ducks/form/selectors'
import { registerFieldExtra, unRegisterExtraField } from '../../../../ducks/form/store'
import { getModelByPrefixAndNameSelector } from '../../../../ducks/models/selectors'
import { useFormContext } from '../../../core/FormProvider'
import { setFieldSubmit } from '../../../../ducks/datasource/store'
import { getValidationClass } from '../../../../core/utils/getValidationClass'
import { useResolved } from '../../../../core/Expression/useResolver'
import { useDispatch } from '../../../../core/Redux/useDispatch'
import { ArrayFieldContext } from '../../../../core/datasource/ArrayField/Context'
import { getDefaultField } from '../../../../ducks/form/FormPlugin'
import { useScrollToFirstInvalid } from '../../../pages/PageScroll'

import { modifyDependencies, replaceIndex, resolveControlIndexes } from './utils'

const useReduxField = ({ name: fieldName, ...fieldProps }, formName, dispatch) => {
    const field = useSelector(makeFieldByName(formName, fieldName))

    useEffect(() => {
        const { isInit } = field

        if (!isInit) {
            dispatch(registerFieldExtra(formName, fieldName, fieldProps))
        }
    }, [formName, fieldName, dispatch, field, fieldProps])

    useEffect(() => () => {
        dispatch(unRegisterExtraField(formName, fieldName))
    }, [formName, fieldName, dispatch])

    if (field.isInit) {
        return field
    }

    return {
        ...getDefaultField(),
        ...fieldProps,
    }
}

const useValidation = (datasource, prefix, fieldName) => {
    const message = useSelector(messageSelector(datasource, fieldName, prefix))

    return {
        message,
        validationClass: getValidationClass(message),
    }
}

const useParentIndex = (props) => {
    const { parentName, model, subMenu, control, action, dependency } = props
    const multiContext = useContext(ArrayFieldContext)

    const resolvedModel = useMemo(() => {
        if (isNil(parentName)) {
            return model
        }

        return {
            // Для дальнейшего доступа значения модели в полях через model[fieldName], без прокидывания parentName и прочего
            ...get(model, parentName),
            ...model,
        }
    }, [model, parentName])
    const resolvedProps = useMemo(() => {
        if (isEmpty(multiContext)) {
            return props
        }

        return {
            ...props,
            control: control && resolveControlIndexes(control, multiContext),
            action: action && replaceIndex(action, multiContext),
            subMenu: subMenu?.map((option) => {
                const { action } = option

                if (action) {
                    option.action = replaceIndex(action, multiContext)
                }

                return option
            }),
            dependency: modifyDependencies(dependency, multiContext),
        }
    }, [action, control, dependency, multiContext, props, subMenu])

    return {
        ...resolvedProps,
        model: resolvedModel,
    }
}

const useResolvedProps = ({ input, meta, model, ...rest }) => {
    const resolved = useResolved(rest, model, ['toolbar', 'html', 'content', 'meta'])

    return {
        ...resolved,
        ...meta,
        model,
        ...input,
    }
}

const useAutosave = (datasource, fieldName, dataProvider, dispatch) => {
    useEffect(() => {
        if (datasource && !isEmpty(dataProvider)) {
            dispatch(setFieldSubmit(datasource, fieldName, dataProvider))
        }
    }, [dispatch, dataProvider, fieldName, datasource])
}

/**
 * HOC обертка для полей, в которой содержится мэппинг свойств редакса и регистрация дополнительных свойств полей
 * @param Field
 * @returns {*}
 */
export default (Field) => {
    const FieldContainer = (props) => {
        const dispatch = useDispatch()
        const { datasource, prefix, formName } = useFormContext()
        const model = useSelector(getModelByPrefixAndNameSelector(prefix, datasource)) || {}
        const withIndex = useParentIndex({
            ...props,
            model,
        })
        const {
            visible, disabled, enabled, multiSetDisabled,
            validation, required, dependency,
            name,
        } = withIndex
        // FIXME разобраться со всеми disabled/enabled, привести всё к единному виду
        const disabledToRegister = isBoolean(enabled) && !disabled
            ? !enabled
            : disabled
        const field = useReduxField({
            name,
            visible,
            disabled: disabledToRegister && enabled === false,
            visible_field: visible,
            disabled_field: disabledToRegister,
            dependency,
            required,
            validation,
        }, formName, dispatch)
        const message = useValidation(datasource, prefix, name)
        const scrollRef = useRef(null)
        const resolved = useResolvedProps({
            ...withIndex,
            ...field,
            disabled: multiSetDisabled || field.disabled,
        })

        useScrollToFirstInvalid(scrollRef, resolved.id, message)
        useAutosave(datasource, resolved.id, resolved.dataProvider, dispatch)

        return <Field {...resolved} {...message} scrollRef={scrollRef} />
    }

    FieldContainer.propTypes = {
        id: PropTypes.string,
        mapProps: PropTypes.func,
        input: PropTypes.object,
    }

    return FieldContainer
}
