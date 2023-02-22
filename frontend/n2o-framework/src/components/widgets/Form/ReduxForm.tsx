import React, { useCallback, Children, FC, CSSProperties } from 'react'
import { Prompt } from 'react-router-dom'
import { useTranslation } from 'react-i18next'

import { FormProvider } from '../../core/FormProvider'
import { ModelPrefix } from '../../../core/datasource/const'

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
    fieldsets: unknown[]
    autoFocus?(): void
    autoSubmit?: boolean
    prompt?: boolean
    style?: CSSProperties
    form: string
    modelPrefix: ModelPrefix
    dirty?: boolean
    initialValues?: object
    activeModel?: object
    className?: string
}

const ReduxForm: FC<TReduxFormProps> & { Field: JSX.Element } = (props) => {
    const { t } = useTranslation()
    const {
        fieldsets,
        autoFocus,
        form,
        modelPrefix,
        autoSubmit,
        activeModel,
        className,
        style,
        prompt = false,
        dirty,
        children,
        initialValues,
    } = props

    const renderFieldSets = useCallback(() => {
        const autoFocusId = autoFocus && getAutoFocusId(flatFields(fieldsets, []))

        return fieldsets.map((fieldset, i) => (
            <Fieldset
                activeModel={activeModel}
                key={i.toString()}
                autoFocusId={autoFocusId}
                form={form}
                modelPrefix={modelPrefix}
                autoSubmit={autoSubmit}
                // @ts-ignore Добавить типизацию
                {...fieldset}
            />
        ))
    }, [activeModel, autoFocus, autoSubmit, fieldsets, form, modelPrefix])
    const hasChildren = Children.count(children)

    return (
        <FormProvider prefix={modelPrefix} name={form} initialValues={initialValues}>
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
