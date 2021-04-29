import React from 'react'
import classNames from 'classnames'
import { compose } from 'recompose'
import { HotKeys } from 'react-hotkeys/cjs'
import PropTypes from 'prop-types'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
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
        if (
            prevProps.editable !== this.props.editable &&
      !isEqual(prevProps.model, this.props.model) &&
      !isEqual(this.state.model, this.props.prevResolveModel)
        ) {
            this.setState({ model: this.props.model })
        } else if (!isEqual(prevProps.model, this.props.model)) {
            this.setState({
                prevModel: this.state.model,
                model: isEmpty(this.props.prevResolveModel)
                    ? this.props.model
                    : this.props.prevResolveModel,
            })
        } else if (
            !isEqual(prevProps.prevResolveModel, this.props.prevResolveModel) &&
      this.props.prevResolveModel.id === this.state.model.id
        ) {
            this.setState({
                prevModel: this.state.model,
                model: this.props.prevResolveModel,
            })
        }

        if (
            !this.state.editing &&
      isEqual(prevState.prevModel, prevState.model) &&
      !isEqual(this.state.prevModel, this.state.model) &&
      !isEqual(this.state.model, this.props.prevResolveModel)
        ) {
            this.callAction(this.state.model)
        }
    }

    onChange(value) {
        const newModel = { ...this.state.model }
        const { editFieldId } = this.props

        set(newModel, editFieldId, value)
        this.setState({
            model: newModel,
        })
    }

    toggleEdit() {
        const { model, prevResolveModel, onSetSelectedId } = this.props
        let newState = {
            editing: !this.state.editing,
        }

        if (!isEqual(get(prevResolveModel, 'id'), get(model, 'id'))) {
            onSetSelectedId()
        }
        if (!newState.editing && !isEqual(this.state.prevModel, this.state.model)) {
            this.callAction(this.state.model)
        }

        newState = {
            ...newState,
            prevModel: this.state.model,
        }

        this.setState(newState)
    }

    callAction(model) {
        const { callAction, onResolve, widgetId } = this.props

        onResolve(widgetId, model)
        callAction(model)
    }

    handleKeyDown() {
        this.toggleEdit()
    }

    stopPropagation(e) {
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
    value: PropTypes.string,
    disabled: PropTypes.bool,
    valueFieldId: PropTypes.string,
    editFieldId: PropTypes.string,
}

EditableCell.defaultProps = {
    visible: true,
    disabled: false,
}

export default compose(
    withActionsEditableCell,
    withCell,
    withTooltip,
)(EditableCell)
