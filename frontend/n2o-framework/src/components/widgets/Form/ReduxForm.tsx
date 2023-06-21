import React, { useCallback, Children, FC, CSSProperties } from 'react'
import { Prompt } from 'react-router-dom'
import { useTranslation } from 'react-i18next'
import { useSelector } from 'react-redux'

import { getModelByPrefixAndNameSelector } from '../../../ducks/models/selectors'
import { FormProvider } from '../../core/FormProvider'
import { ModelPrefix } from '../../../core/datasource/const'
import { ValidationsKey } from '../../../core/validation/IValidation'

// @ts-ignore ignore import error from js file
import ReduxField from './ReduxField'
// @ts-ignore ignore import error from js file
import { flatFields, getAutoFocusId } from './utils'
// @ts-ignore ignore import error from js file
import Fieldset from './Fieldset'

/**
 *
 * @reactProps {object} props.prompt - флаг включения обработки выхода с несохраненной формы
 * @reactProps {object} props.dirty - флаг отличия формы от initialState
 * @returns {*}
 * @constructor
 */

type TReduxFormProps = {
    name: string
    datasource: string
    modelPrefix: ModelPrefix
    fieldsets: unknown[]
    autoFocus?(): void
    autoSubmit?: boolean
    prompt?: boolean
    style?: CSSProperties
    dirty?: boolean
    className?: string
    validationKey: ValidationsKey
}

const ReduxForm: FC<TReduxFormProps> & { Field: JSX.Element } = (props) => {
    const { t } = useTranslation()
    const {
        name: formName,
        datasource,
        fieldsets,
        autoFocus,
        modelPrefix,
        autoSubmit,
        className,
        style,
        prompt = false,
        dirty,
        children,
        validationKey,
    } = props
    const activeModel = useSelector(getModelByPrefixAndNameSelector(modelPrefix, datasource))

    const renderFieldSets = useCallback(() => {
        const autoFocusId = autoFocus && getAutoFocusId(flatFields(fieldsets, []))

        return fieldsets.map((fieldset, i) => (
            <Fieldset
                activeModel={activeModel}
                key={i.toString()}
                autoFocusId={autoFocusId}
                modelPrefix={modelPrefix}
                autoSubmit={autoSubmit}
                // @ts-ignore Добавить типизацию
                {...fieldset}
            />
        ))
    }, [activeModel, autoFocus, autoSubmit, fieldsets, modelPrefix])
    const hasChildren = Children.count(children)

    return (
        <FormProvider
            prefix={modelPrefix}
            formName={formName}
            datasource={datasource}
            validationKey={validationKey}
        >
            {prompt && (
                <Prompt when={dirty} message={t('defaultPromptMessage')} />
            )}
            <div className={className} style={style}>
                {hasChildren ? children : renderFieldSets()}
            </div>
        </FormProvider>
    )
}

ReduxForm.Field = ReduxField

export default ReduxForm
