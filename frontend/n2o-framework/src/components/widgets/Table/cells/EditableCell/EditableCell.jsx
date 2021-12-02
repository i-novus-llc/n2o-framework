import React from 'react'
import classNames from 'classnames'
import { compose } from 'recompose'
import { HotKeys } from 'react-hotkeys/cjs'
import PropTypes from 'prop-types'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
import cloneDeep from 'lodash/cloneDeep'
import get from 'lodash/get'
import set from 'lodash/set'

import Text from '../../../../snippets/Text/Text'
import withCell from '../../withCell'
import withTooltip from '../../withTooltip'

import withActionsEditableCell from './withActionsEditableCell'
/**
 * Компонент редактируемой ячейки таблицы
 * @reactProps {boolean} visible - флаг видимости
 * @reactProps {object} control - настройки компонента фильтрации
 * @reactProps {boolean} editable - флаг разрешения редактирования
 * @reactProps {string} model - значение модели
 * @reactProps {boolean} disabled - флаг активности
 * @reactProps {string} strategy - стиль вывода popper fixed
 */
export class EditableCell extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            model: props.model || {},
            editing: false,
            prevModel: {},
        }

        this.onChange = this.onChange.bind(this)
        this.toggleEdit = this.toggleEdit.bind(this)
        this.handleKeyDown = this.handleKeyDown.bind(this)
        this.callAction = this.callAction.bind(this)
    }

    componentDidUpdate(prevProps, prevState) {
        const { model: propsModel, editable, prevResolveModel } = this.props
        const { model: stateModel, editing, prevModel } = this.state

        if (
            prevProps.editable !== editable &&
            !isEqual(prevProps.model, propsModel) &&
            !isEqual(stateModel, prevResolveModel)
        ) {
            this.setState({ model: propsModel })
        } else if (!isEqual(prevProps.model, propsModel)) {
            this.setState({
                prevModel: stateModel,
                model: isEmpty(prevResolveModel)
                    ? propsModel
                    : prevResolveModel,
            })
        } else if (
            !isEqual(prevProps.prevResolveModel, prevResolveModel) &&
            prevResolveModel.id === stateModel.id
        ) {
            this.setState({
                prevModel: stateModel,
                model: prevResolveModel,
            })
        }

        if (
            !editing &&
            isEqual(prevState.prevModel, prevState.model) &&
            !isEqual(prevModel, stateModel) &&
            !isEqual(stateModel, prevResolveModel)
        ) {
            this.callAction(stateModel)
        }
    }

    onChange(value) {
        const { model: stateModel } = this.state
        const newModel = cloneDeep(stateModel)
        const { editFieldId } = this.props

        set(newModel, editFieldId, value)
        this.setState({
            model: newModel,
        })
    }

    toggleEdit() {
        const { editing, prevModel, model: stateModel } = this.state
        let newState = {
            editing: !editing,
        }

        if (!newState.editing && !isEqual(prevModel, stateModel)) {
            this.callAction(stateModel)
        }

        newState = {
            ...newState,
            prevModel: stateModel,
        }

        this.setState(newState)
    }

    callAction(model) {
        const { callAction, onResolve, modelId } = this.props

        onResolve(modelId, model)
        callAction(model)
    }

    handleKeyDown() {
        this.toggleEdit()
    }

    stopPropagation = (e) => {
        e.stopPropagation()
    }

    render() {
        const {
            visible,
            control,
            editable,
            format,
            fieldKey,
            editFieldId,
        } = this.props
        const { editing, model } = this.state
        const events = { events: 'enter' }
        const handlers = { events: this.handleKeyDown }
        const text = get(model, fieldKey)

        return (
            visible && (
                <div
                    className={classNames({ 'n2o-editable-cell': editable })}
                    onClick={this.stopPropagation}
                >
                    {!editing && (
                        <div
                            className={classNames('n2o-editable-cell-text', {
                                'editable-cell-empty': !text,
                            })}
                            onClick={editable && this.toggleEdit}
                        >
                            <Text text={text} format={format} />
                        </div>
                    )}
                    {editable && editing && (
                        <HotKeys keyMap={events} handlers={handlers}>
                            <div className="n2o-editable-cell-control">
                                {React.createElement(control.component, {
                                    ...control,
                                    className: 'n2o-advanced-table-edit-control',
                                    onChange: this.onChange,
                                    onBlur: this.toggleEdit,
                                    autoFocus: true,
                                    value: get(model, editFieldId),
                                    openOnFocus: true,
                                    showButtons: false,
                                    resetOnNotValid: false,
                                    strategy: 'fixed',
                                })}
                            </div>
                        </HotKeys>
                    )}
                </div>
            )
        )
    }
}

EditableCell.propTypes = {
    visible: PropTypes.bool,
    control: PropTypes.object,
    editable: PropTypes.bool,
    editFieldId: PropTypes.string,
    model: PropTypes.object,
    prevResolveModel: PropTypes.object,
    callAction: PropTypes.func,
    onResolve: PropTypes.func,
    // eslint-disable-next-line react/no-unused-prop-types
    widgetId: PropTypes.string,
    modelId: PropTypes.string,
    format: PropTypes.string,
    fieldKey: PropTypes.string,
}

EditableCell.defaultProps = {
    visible: true,
}

export default compose(
    withActionsEditableCell,
    withCell,
    withTooltip,
)(EditableCell)
