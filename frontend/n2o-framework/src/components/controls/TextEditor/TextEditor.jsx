import React, { Component } from 'react'
import cn from 'classnames'
import isEqual from 'lodash/isEqual'
import { EditorState, convertToRaw, ContentState } from 'draft-js'
import { Editor } from 'react-draft-wysiwyg'
import draftToHtml from 'draftjs-to-html'
import htmlToDraft from 'html-to-draftjs'
import PropTypes from 'prop-types'

/**
 * Компонент TextEditor
 * @param value - значение разметки
 * @param onChange - callback на изменение значения
 * @param onFocus - callback на фокус
 * @param onBlur - callback на потерю фокуса
 * @param disabled - флаг активности
 * @param visible - флаг видимости
 * @param className - класс компонента
 * @param toolbarConfig - настройка тулбара
 *
 * @example
 * toolbar: {
    inline: { inDropdown: true },
    list: { inDropdown: true },
    textAlign: { inDropdown: true },
    link: { inDropdown: true },
    history: { inDropdown: true },
  }
 */
class TextEditor extends Component {
    constructor(props) {
        super(props)

        this.state = {
            editorState: this.convertToEditorState(props.value),
            // eslint-disable-next-line react/no-unused-state
            value: props.value,
        }

        this.onEditorStateChange = this.onEditorStateChange.bind(this)
    }

    componentDidUpdate(prevProps) {
        const { value } = this.props

        if (!isEqual(prevProps.value, value)) {
            this.setState({
                editorState: EditorState.moveFocusToEnd(
                    this.convertToEditorState(value),
                ),
                // eslint-disable-next-line react/no-unused-state
                value,
            })
        }
    }

    convertToHtml = editorState => (
        draftToHtml(convertToRaw(editorState.getCurrentContent()))
    )

    convertToEditorState = (value) => {
        const contentBlock = htmlToDraft(value)

        if (contentBlock) {
            const contentState = ContentState.createFromBlockArray(
                contentBlock.contentBlocks,
            )

            return EditorState.createWithContent(contentState)
        }

        return EditorState.createEmpty()
    }

    onEditorStateChange(editorState) {
        const { onChange } = this.props
        const value = this.convertToHtml(editorState)

        if (onChange) {
            onChange(value)
        }
        // eslint-disable-next-line react/no-unused-state
        this.setState({ editorState, value })
    }

    render() {
        const {
            className,
            disabled,
            visible,
            onFocus,
            onBlur,
            toolbarConfig,
        } = this.props
        const { editorState } = this.state
        const baseStyle = {
            wordBreak: 'break-all',
            wordWrap: 'break-word',
            maxWidth: '100%',
        }
        const disabledStyle = {
            pointerEvents: 'none',
            opacity: '0.4',
        }

        return (
            <div style={disabled ? { ...baseStyle, ...disabledStyle } : baseStyle}>
                {visible && (
                    <Editor
                        onFocus={onFocus}
                        onBlur={onBlur}
                        editorState={editorState}
                        wrapperClassName={cn('n2o-text-editor-wrapper')}
                        editorClassName={cn('n2o-text-editor', className)}
                        onEditorStateChange={this.onEditorStateChange}
                        toolbar={toolbarConfig}
                    />
                )}
            </div>
        )
    }
}

TextEditor.propTypes = {
    /**
     * Значение
     */
    value: PropTypes.string,
    /**
     * Callback на изменение
     */
    onChange: PropTypes.func,
    /**
     * Callback на фокус
     */
    onFocus: PropTypes.func,
    /**
     * Callback на потерю фокуса
     */
    onBlur: PropTypes.func,
    /**
     * Флаг активности
     */
    disabled: PropTypes.bool,
    /**
     * Флаг видимости
     */
    visible: PropTypes.bool,
    /**
     * Класс
     */
    className: PropTypes.string,
    /**
     * Конфиг тулбара
     */
    toolbarConfig: PropTypes.object,
}
TextEditor.defaultProps = {
    onChange: () => {},
    onFocus: () => {},
    onBlur: () => {},
    disabled: false,
    visible: true,
    value: '',
}

export default TextEditor
