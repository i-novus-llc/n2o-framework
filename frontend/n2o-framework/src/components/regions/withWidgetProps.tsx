// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-nocheck ошибка генерации d.ts FIXME типизировать
import React, { FC } from 'react'
import { connect, ReactReduxContext } from 'react-redux'
import { bindActionCreators } from 'redux'
import omit from 'lodash/omit'
import get from 'lodash/get'

import { makeModelIdSelector, widgetsSelector } from '../../ducks/widgets/selectors'
import {
    makeModelsByPrefixSelector,
} from '../../ducks/models/selectors'
import { makePageMetadataByIdSelector } from '../../ducks/pages/selectors'
import {
    hideWidget,
    showWidget,
    disableWidget,
    enableWidget,
} from '../../ducks/widgets/store'
import { ModelPrefix } from '../../core/datasource/const'

/**
 * HOC для работы с данными
 * @param WrappedComponent - оборачиваемый компонент
 */

function withGetWidget(WrappedComponent: FC) {
    class WithGetWidget extends React.Component {
        constructor(props) {
            super(props)

            this.getWidget = this.getWidget.bind(this)
            this.getWidgetProps = this.getWidgetProps.bind(this)
        }

        getWidget(pageId: string, widgetId: string) {
            const { store } = this.context
            const state = store.getState()

            return get(makePageMetadataByIdSelector(pageId)(state), [
                'widgets',
                widgetId,
            ])
        }

        getWidgetProps(widgetId: string) {
            const { widgets, widgetsDatasource } = this.props

            return {
                ...get(widgets, widgetId, {}),
                datasource: widgetsDatasource[widgetId],
            }
        }

        render() {
            const props = omit(this.props, ['widgets'])

            return (
                <WrappedComponent
                    {...props}
                    getWidget={this.getWidget}
                    getWidgetProps={this.getWidgetProps}
                />
            )
        }
    }

    WithGetWidget.contextType = ReactReduxContext

    const mapStateToProps = (state, { widgetId }) => ({
        widgets: widgetsSelector(state),
        widgetsDatasource: makeModelsByPrefixSelector(ModelPrefix.source)(state),
        modelId: makeModelIdSelector(widgetId)(state),
    })

    const mapDispatchToProps = dispatch => bindActionCreators(
        {
            hideWidget: widgetId => hideWidget(widgetId),
            showWidget: widgetId => showWidget(widgetId),
            disableWidget: widgetId => disableWidget(widgetId),
            enableWidget: widgetId => enableWidget(widgetId),
        },
        dispatch,
    )

    return connect(
        mapStateToProps,
        mapDispatchToProps,
    )(WithGetWidget)
}

export default withGetWidget as never
