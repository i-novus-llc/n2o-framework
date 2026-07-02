import React, { useCallback, Children, FC } from 'react'
import { Prompt } from 'react-router-dom'
import { useTranslation } from 'react-i18next'
import { useSelector } from 'react-redux'

import { getModelSelector, Model } from '../../../ducks/models/selectors'
import { FormProvider } from '../../core/FormProvider'
import { EMPTY_OBJECT } from '../../../utils/emptyTypes'

import { flatFields, getAutoFocusId } from './utils'
import { FieldsetContainer as Fieldset } from './Fieldset'
import { type ReduxFormProps } from './types'

/**
 *
 * @reactProps {object} props.prompt - флаг включения обработки выхода с несохраненной формы
 * @reactProps {object} props.dirty - флаг отличия формы от initialState
 * @constructor
 */

export const ReduxForm: FC<ReduxFormProps> = ({
    name: formName,
    fieldsets,
    autoFocus,
    modelLink,
    autoSubmit,
    className,
    style,
    prompt = false,
    dirty,
    children,
    needActiveModel,
}) => {
    const { t } = useTranslation()

    const activeModel = useSelector(getModelSelector<Model>(modelLink))

    const renderFieldSets = useCallback(() => {
        const autoFocusId = autoFocus && getAutoFocusId(flatFields(fieldsets))

        return fieldsets.map((fieldset, i) => (
            <Fieldset
                activeModel={activeModel || EMPTY_OBJECT}
                /* eslint-disable-next-line react/no-array-index-key */
                key={i.toString()}
                autoFocusId={autoFocusId}
                {...fieldset}
                autoSubmit={autoSubmit}
            />
        ))
    }, [activeModel, autoFocus, autoSubmit, fieldsets])
    const hasChildren = Children.count(children)

    return (
        <FormProvider
            formName={formName}
            modelLink={modelLink}
            needActiveModel={needActiveModel}
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
