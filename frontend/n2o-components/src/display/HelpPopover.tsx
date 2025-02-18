import React, { RefObject, createRef, MouseEvent } from 'react'
import { UncontrolledPopover, PopoverBody, PopoverProps } from 'reactstrap'

import { id } from '../utils/id'
import { Text } from '../Typography/Text'

type Props = {
    help: string | null,
    icon: string,
    placement: PopoverProps['placement']
}

export class HelpPopover extends React.Component<Props> {
    fieldId: string

    button: RefObject<HTMLButtonElement>

    constructor(props: Props) {
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
        const { help, placement, icon } = this.props

        if (!help) { return null }

        return (
            <div className="n2o-popover">
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
                    placement={placement}
                    target={this.fieldId}
                    trigger="focus"
                >
                    <PopoverBody>
                        {
                            typeof help === 'string'
                                ? <Text>{help}</Text>
                                /* eslint-disable-next-line react/no-danger */
                                : <div dangerouslySetInnerHTML={{ __html: help }} />
                        }
                    </PopoverBody>
                </UncontrolledPopover>
            </div>
        )
    }

    static defaultProps = {
        placement: 'right',
        icon: 'fa fa-question-circle',
    } as Props

    static displayName = '@n2o-components/display/HelpPopover'
}
