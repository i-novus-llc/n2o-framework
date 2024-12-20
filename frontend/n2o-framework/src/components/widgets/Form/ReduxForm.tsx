import React, { useCallback, Children, FC } from 'react'
import { Prompt } from 'react-router-dom'
import { useTranslation } from 'react-i18next'
import { useSelector } from 'react-redux'

import { getModelByPrefixAndNameSelector } from '../../../ducks/models/selectors'
import { FormProvider } from '../../core/FormProvider'

import ReduxField from './ReduxField'
import { flatFields, getAutoFocusId } from './utils'
import { FieldsetContainer as Fieldset } from './Fieldset'
import { type ReduxFormProps } from './types'

/**
 *
 * @reactProps {object} props.prompt - флаг включения обработки выхода с несохраненной формы
 * @reactProps {object} props.dirty - флаг отличия формы от initialState
 * @constructor
 */

const ReduxForm: FC<ReduxFormProps> & { Field: JSX.Element } = ({
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
}) => {
    const { t } = useTranslation()

    const activeModel = useSelector(getModelByPrefixAndNameSelector(modelPrefix, datasource))

    const renderFieldSets = useCallback(() => {
        const autoFocusId = autoFocus && getAutoFocusId(flatFields(fieldsets))

        return fieldsets.map((fieldset, i) => (
            <Fieldset
                activeModel={activeModel}
                /* eslint-disable-next-line react/no-array-index-key */
                key={i.toString()}
                autoFocusId={autoFocusId}
                modelPrefix={modelPrefix}
                {...fieldset}
                autoSubmit={autoSubmit}
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
