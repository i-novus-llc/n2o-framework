import React from 'react'
import PropTypes from 'prop-types'
import { connect, ReactReduxContext } from 'react-redux'
import { bindActionCreators } from 'redux'
import omit from 'lodash/omit'
import get from 'lodash/get'

import { makeModelIdSelector, widgetsSelector } from '../../ducks/widgets/selectors'
import {
    makeModelsByPrefixSelector,
    modelsSelector,
} from '../../ducks/models/selectors'
import { pagesSelector, makePageMetadataByIdSelector } from '../../ducks/pages/selectors'
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

function withGetWidget(WrappedComponent) {
    class WithGetWidget extends React.Component {
        constructor(props) {
            super(props)

            this.getWidget = this.getWidget.bind(this)
            this.getWidgetProps = this.getWidgetProps.bind(this)
        }

        getWidget(pageId, widgetId) {
            const { store } = this.context
            const state = store.getState()

            return get(makePageMetadataByIdSelector(pageId)(state), [
                'widgets',
                widgetId,
            ])
        }

        getWidgetProps(widgetId) {
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

    WithGetWidget.propTypes = {
        pages: PropTypes.object,
        widgetsDatasource: PropTypes.object,
        widgets: PropTypes.object,
        hideWidget: PropTypes.func,
        showWidget: PropTypes.func,
        disableWidget: PropTypes.func,
        enableWidget: PropTypes.func,
    }

    WithGetWidget.contextType = ReactReduxContext

    const mapStateToProps = (state, props) => ({
        pages: pagesSelector(state),
        widgets: widgetsSelector(state),
        widgetsDatasource: makeModelsByPrefixSelector(ModelPrefix.source)(state),
        models: modelsSelector(state),
        modelId: makeModelIdSelector(props.widgetId)(state),
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

export default withGetWidget
