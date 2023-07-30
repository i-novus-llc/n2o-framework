import React, { Component, ReactNode, RefObject, SyntheticEvent, createRef } from 'react'

import { TBaseProps } from '../../types'

import { moveCursorToEnd } from './utils'

type Props = TBaseProps & {
    children: ReactNode,
    editable: boolean,
    onChange(event: SyntheticEvent): void,
}

export class ContentEditable extends Component<Props> {
    ref: RefObject<HTMLDivElement>

    constructor(props: Props) {
        super(props)
        this.ref = createRef()
    }

    componentDidUpdate(prevProps: Props) {
        const { editable } = this.props

        if (editable !== prevProps.editable && editable) {
            this.ref.current?.focus()
            moveCursorToEnd(this.ref.current)
        }
    }

    render() {
        const { editable, children, onChange } = this.props

        return editable ? (
            <div
                className="editable"
                ref={this.ref}
                contentEditable={editable}
                onInput={onChange}
            >
                {children}
            </div>
        ) : (
            children
        )
    }

    static defaultProps = {
        editable: false,
        children: null,
        onChange: () => {},
    } as Props
}
