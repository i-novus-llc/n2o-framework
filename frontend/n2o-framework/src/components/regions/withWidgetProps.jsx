import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import omit from 'lodash/omit'
import get from 'lodash/get'
import reduce from 'lodash/reduce'
import isEmpty from 'lodash/isEmpty'

import { makeModelIdSelector, widgetsSelector } from '../../ducks/widgets/selectors'
import {
    makeModelsByPrefixSelector,
    modelsSelector,
    getModelsByDependency,
} from '../../ducks/models/selectors'
import { pagesSelector, makePageMetadataByIdSelector } from '../../ducks/pages/selectors'
import {
    hideWidget,
    showWidget,
    disableWidget,
    enableWidget,
} from '../../ducks/widgets/store'
import { PREFIXES } from '../../ducks/models/constants'
import { reduceFunction } from '../../sagas/widgetDependency/resolve'

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
            this.getVisible = this.getVisible.bind(this)
        }

        getWidget(pageId, widgetId) {
            const { store } = this.context
            const state = store.getState()

            return get(makePageMetadataByIdSelector(pageId)(state), [
                'widgets',
                widgetId,
            ])
        }

        getVisible(pageId, widgetId) {
            const { store } = this.context
            const dependencies = get(
                this.props,
                `pages[${pageId}].metadata.widgets[${widgetId}].dependency.visible`,
                [],
            )

            if (isEmpty(dependencies)) {
                return true
            }

            const model = getModelsByDependency(dependencies)(
                store.getState(),
            )

            return reduce(model, reduceFunction, true)
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
                    getVisible={this.getVisible}
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

    WithGetWidget.contextTypes = {
        store: PropTypes.object,
    }

    const mapStateToProps = (state, props) => ({
        pages: pagesSelector(state),
        widgets: widgetsSelector(state),
        widgetsDatasource: makeModelsByPrefixSelector(PREFIXES.datasource)(state),
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
