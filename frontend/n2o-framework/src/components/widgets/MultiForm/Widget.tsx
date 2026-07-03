import React, { useContext, useMemo } from 'react'
import { useSelector, useStore } from 'react-redux'
import get from 'lodash/get'
import i18next from 'i18next'

import StandardWidget from '../StandardWidget'
import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { FactoryContext } from '../../../core/factory/context'
import { getModelByPrefixAndNameSelector } from '../../../ducks/models/selectors'
import { ModelPrefix } from '../../../core/models/types'
import { getKey } from '../../../utils/uniqueKey'
import { isDirtyForm } from '../../../ducks/form/selectors'
import Fieldsets from '../Form/fieldsets'
import { ReduxForm } from '../Form/ReduxForm'
import { FieldSetsProps, type FormWidgetProps } from '../Form/types'
import { N2OPagination } from '../Table/N2OPagination'
import { ArrayFieldProvider } from '../../../core/datasource/ArrayField/ArrayFieldProvider'
import { RowProvider } from '../../../core/datasource/ArrayField/RowProvider'

type Props = FormWidgetProps & {
    size: number
    count: number
    page: number
    setPage(page: number): void
}

const Widget = ({
    id: formName,
    disabled,
    toolbar,
    datasource = formName,
    className,
    style,
    form,
    loading,
    filter,
    paging,
    size,
    setPage,
    page = 1,
    count,
}: Props) => {
    const { resolveProps } = useContext(FactoryContext)
    const fieldsets = useMemo(
        () => (resolveProps<FieldSetsProps>(form.fieldsets, Fieldsets.StandardFieldset)),
        [form, resolveProps],
    )
    const resolvedFilter = useMemo(
        () => filter && resolveProps<FormWidgetProps['filter']>(filter, Fieldsets.StandardFieldset),
        [filter, resolveProps],
    )
    const { prompt } = form
    const store = useStore()
    const models = useSelector(getModelByPrefixAndNameSelector(ModelPrefix.source, datasource))
    const forms = useMemo(() => models?.map((model, index) => {
        return ({
            name: `${formName}[${getKey(model)}]`,
            modelLink: {
                id: datasource,
                prefix: ModelPrefix.source,
                index,
            },
        })
    }) ?? [], [models, datasource, formName])

    const place = get(paging, 'place', 'bottomLeft')
    const updatePage = (page: number) => {
        const state = store.getState()
        const hasDirty = prompt && forms.some(({ name }) => isDirtyForm(name)(state))

        // eslint-disable-next-line no-alert
        if (hasDirty && !window.confirm(i18next.t('defaultPromptMessage'))) {
            return
        }

        setPage(page)
    }
    const pagination = {
        [place]: (
            <N2OPagination
                {...paging}
                size={size}
                activePage={page}
                datasource={models}
                datasourceId={datasource}
                setPage={updatePage}
                count={count}
            />
        ),
    }

    return (
        <StandardWidget
            className={className}
            datasource={datasource}
            disabled={disabled}
            filter={resolvedFilter}
            loading={loading}
            pagination={pagination}
            style={style}
            toolbar={toolbar}
            widgetId={formName}
        >
            <ArrayFieldProvider>
                {forms.map(form => (
                    <RowProvider index={form.modelLink.index} key={form.name}>
                        <ReduxForm
                            {...form}
                            dirty={isDirtyForm(formName)(store.getState())}
                            fieldsets={fieldsets}
                            prompt={prompt}
                        />
                    </RowProvider>
                ))}
            </ArrayFieldProvider>
        </StandardWidget>
    )
}

Widget.displayName = 'FormWidgetComponent'

export const MultiFormWidget = WidgetHOC<Props>(Widget)
export default MultiFormWidget

MultiFormWidget.displayName = 'MultiFormWidget'
