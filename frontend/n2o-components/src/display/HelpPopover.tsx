import React, { RefObject, createRef, MouseEvent } from 'react'
import { UncontrolledPopover, PopoverBody, PopoverProps } from 'reactstrap'
import DOMPurify from 'dompurify'
import classNames from 'classnames'

import { id } from '../utils/id'
import { Text } from '../Typography/Text'
import { containsHtml } from '../utils/containsHtml'

export enum HelpTrigger {
    HOVER = 'hover',
    FOCUS = 'focus',
    CLICK = 'click',
    LEGACY = 'legacy',
}

export interface HelpPopoverProps {
    help?: string | null | Node
    icon?: string,
    helpPlacement?: PopoverProps['placement']
    helpTrigger?: HelpTrigger
    className?: string
}

export class HelpPopover extends React.Component<HelpPopoverProps> {
    fieldId: string

    button: RefObject<HTMLButtonElement>

    constructor(props: HelpPopoverProps) {
        super(props)

        this.fieldId = id()
        this.button = createRef()
    }

    focusOnClick = (e: MouseEvent<HTMLButtonElement>) => {
        e.stopPropagation()
        e.preventDefault()
        this.button.current?.focus()
    }

    render() {
        const {
            help,
            className,
            helpTrigger = 'focus',
            helpPlacement = 'right',
            icon = 'fa fa-question-circle',
        } = this.props

        if (!help) { return null }

        return (
            <div className={classNames('n2o-popover', className)}>
                <button
                    onClick={this.focusOnClick}
                    className="n2o-popover-btn"
                    id={this.fieldId}
                    ref={this.button}
                    type="button"
                    aria-label="popover-btn"
                >
                    <i className={icon} />
                </button>
                <UncontrolledPopover
                    className="n2o-popover-body"
                    placement={helpPlacement}
                    trigger={helpTrigger === HelpTrigger.CLICK ? HelpTrigger.LEGACY : helpTrigger}
                    target={this.fieldId}
                >
                    <PopoverBody>
                        {containsHtml(help)
                            ? <div dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(help) }} />
                            : <Text>{help}</Text>}
                    </PopoverBody>
                </UncontrolledPopover>
            </div>
        )
    }

    static displayName = '@n2o-components/display/HelpPopover'
}
