import React, { useContext, useEffect, useMemo, useRef } from 'react'
import PropTypes from 'prop-types'
import { useSelector } from 'react-redux'
import isBoolean from 'lodash/isBoolean'
import get from 'lodash/get'
import isNil from 'lodash/isNil'
import isEmpty from 'lodash/isEmpty'

import { makeFieldByName, messageSelector } from '../../../../ducks/form/selectors'
import { registerFieldExtra } from '../../../../ducks/form/store'
import { useFormContext } from '../../../core/FormProvider'
import { setFieldSubmit } from '../../../../ducks/datasource/store'
import { getValidationClass } from '../../../../core/utils/getValidationClass'
import { useResolved } from '../../../../core/Expression/useResolver'
import { useDispatch } from '../../../../core/Redux/useDispatch'
import { ArrayFieldContext } from '../../../../core/datasource/ArrayField/Context'
import { getDefaultField } from '../../../../ducks/form/FormPlugin'
import { useScrollToFirstInvalid } from '../../../pages/PageScroll'

import { modifyDependencies, resolveControlIndexes } from './utils'

const useReduxField = ({ name: fieldName, ...fieldProps }, formName, dispatch) => {
    const field = useSelector(makeFieldByName(formName, fieldName))

    useEffect(() => {
        const { isInit } = field

        if (!isInit) {
            dispatch(registerFieldExtra(formName, fieldName, fieldProps))
        }
    }, [formName, fieldName, dispatch, field, fieldProps])

    if (field.isInit) {
        return field
    }

    return {
        ...getDefaultField(),
        ...fieldProps,
    }
}

const useValidation = (fieldLink, formName) => {
    const message = useSelector(messageSelector(fieldLink, formName))

    return {
        message,
        validationClass: getValidationClass(message),
    }
}

const useParentIndex = (props) => {
    const { parentName, model, control, dependency } = props
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
            dependency: modifyDependencies(dependency, multiContext),
        }
    }, [control, dependency, multiContext, props])

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
        const { modelLink, formName, getValues } = useFormContext()
        const { id: datasource } = modelLink
        const model = getValues()
        const withIndex = useParentIndex({ ...props, model })
        const {
            visible, disabled, enabled, multiSetDisabled,
            validation, required, dependency,
            name,
        } = withIndex
        // FIXME разобраться со всеми disabled/enabled, привести всё к единному виду
        const disabledToRegister = isBoolean(enabled) && !disabled
            ? !enabled
            : disabled
        const { rowId } = props
        const field = useReduxField({
            name,
            visible,
            disabled: disabledToRegister && enabled === false,
            visible_field: visible,
            disabled_field: disabledToRegister,
            dependency,
            required,
            validation,
            rowId,
        }, formName, dispatch)

        const message = useValidation({ ...modelLink, field: name }, formName)
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
