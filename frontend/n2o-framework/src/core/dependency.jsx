import React from 'react'
import get from 'lodash/get'
import has from 'lodash/has'
import { connect } from 'react-redux'

import {
    makeWidgetVisibleSelector,
    makeWidgetDisabledSelector,
    makeWidgetIsInitSelector,
} from '../ducks/widgets/selectors'
import { registerDependency } from '../actions/dependency'
import { registerWidget } from '../ducks/widgets/store'

export const InitMetadataContext = React.createContext({
    metadata: {
        visible: true,
    },
})

/**
 * НОС - создает зависимость
 *
 */
/**
 * @type {Function} ReturnedComponent
 */
export const dependency = (WrappedComponent) => {
    class UniversalDependency extends React.Component {
        constructor(props) {
            super(props)

            this.initIfNeeded(props)
        }

        initIfNeeded(props) {
            const {
                registerDependency,
                preInitWidget,
                dependency,
                isInit,
                id: widgetId,
                pageId,
                dataProvider,
                modelPrefix,
            } = props
            const hasVisibleDeps = has(this.props, 'dependency.visible')
            const defaultVisible = get(this.props, 'visible', true)

            if (!isInit) {
                preInitWidget({
                    widgetId,
                    pageId,
                    dataProvider,
                    modelPrefix,
                    visible: hasVisibleDeps ? false : defaultVisible,
                })
                registerDependency(dependency)
            }
        }

        render() {
            const { visible, disabled } = this.props

            const initMetadata = {
                metadata: {
                    visible: get(this.props, 'visible', true),
                    dependency: get(this.props, 'dependency'),
                },
            }

            return (
                <InitMetadataContext.Provider value={initMetadata}>
                    {visible ? (
                        <WrappedComponent
                            {...this.props}
                            disabled={disabled}
                            visible={visible}
                        />
                    ) : null}
                </InitMetadataContext.Provider>
            )
        }
    }

    const mapStateToProps = (state, props) => ({
        isInit: makeWidgetIsInitSelector(props.id)(state),
        visible: makeWidgetVisibleSelector(props.id)(state),
        disabled: makeWidgetDisabledSelector(props.id)(state),
    })

    const mapDispatchToProps = (dispatch, ownProps) => {
        const { id: widgetId, datasource: modelId } = ownProps

        return {
            registerDependency: dependency => dispatch(registerDependency(widgetId, dependency)),
            preInitWidget: options => dispatch(registerWidget(widgetId, { ...options, modelId }, true)),
        }
    }

    return connect(
        mapStateToProps,
        mapDispatchToProps,
    )(UniversalDependency)
}

export default dependency
