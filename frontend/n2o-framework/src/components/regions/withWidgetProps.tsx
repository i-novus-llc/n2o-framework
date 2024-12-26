import React, { ComponentType } from 'react'
import { connect, useStore } from 'react-redux'
import { bindActionCreators, Dispatch } from 'redux'
import omit from 'lodash/omit'
import get from 'lodash/get'

import { makeModelIdSelector, widgetsSelector } from '../../ducks/widgets/selectors'
import { makeModelsByPrefixSelector } from '../../ducks/models/selectors'
import { makePageMetadataByIdSelector } from '../../ducks/pages/selectors'
import {
    hideWidget,
    showWidget,
    disableWidget,
    enableWidget,
} from '../../ducks/widgets/store'
import { ModelPrefix } from '../../core/datasource/const'
import { State } from '../../ducks/State'
import { State as WidgetsState } from '../../ducks/widgets/Widgets'

export interface WithGetWidgetProps {
    widgetId: string
    widgets: WidgetsState
    widgetsDatasource: Record<string, unknown>
    modelId: string
    hideWidget(widgetId: string): void
    showWidget(widgetId: string): void
    disableWidget(widgetId: string): void
    enableWidget(widgetId: string): void
}

export interface WithGetWidgetHOCProps {
    getWidget(pageId: string, widgetId: string): Record<string, unknown>
    getWidgetProps(widgetId: string): Record<string, unknown>
}

// HOC для работы с данными
export function WithGetWidget<P extends WithGetWidgetProps>(WrappedComponent: ComponentType<P>) {
    const Wrapper: ComponentType<P & WithGetWidgetHOCProps> = (props) => {
        const store = useStore()

        const getWidget = (pageId: string, widgetId: string) => {
            const state = store.getState() as State

            return get(makePageMetadataByIdSelector(pageId)(state), ['widgets', widgetId])
        }

        const getWidgetProps = (widgetId: string) => {
            const { widgets, widgetsDatasource } = props

            return { ...get(widgets, widgetId, {}), datasource: widgetsDatasource[widgetId] }
        }

        return (
            <WrappedComponent
                {...omit(props, ['widgets']) as P}
                getWidget={getWidget}
                getWidgetProps={getWidgetProps}
            />
        )
    }

    const mapStateToProps = (state: State, { widgetId }: P) => ({
        widgets: widgetsSelector(state),
        widgetsDatasource: makeModelsByPrefixSelector(ModelPrefix.source)(state),
        modelId: makeModelIdSelector(widgetId)(state),
    })

    const mapDispatchToProps = (dispatch: Dispatch) => bindActionCreators(
        {
            hideWidget: (widgetId: string) => hideWidget(widgetId),
            showWidget: (widgetId: string) => showWidget(widgetId),
            disableWidget: (widgetId: string) => disableWidget(widgetId),
            enableWidget: (widgetId: string) => enableWidget(widgetId),
        },
        dispatch,
    )

    Wrapper.displayName = 'WithGetWidget'

    return connect(mapStateToProps, mapDispatchToProps)(Wrapper as never)
}

export default WithGetWidget
