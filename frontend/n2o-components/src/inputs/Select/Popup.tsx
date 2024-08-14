import React, { ReactNode, RefObject, createRef } from 'react'
import { DropdownMenu, Dropdown } from 'reactstrap'
import classNames from 'classnames'

/**
 * Компонент попапа для {@link InputSelect}
 * @reactProps {boolean} isExpanded - флаг видимости попапа
 * @reactProps {node} children - флаг видимости попапа
 * @reactProps {string} expandPopUp - флаг видимости попапа
 */

interface Props {
    children: ReactNode
    expandPopUp?: string
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    inputSelect: any
    isExpanded: boolean
}

enum Direction {
    down = 'down',
    up = 'up',
}

interface State { direction: Direction }

export class Popup extends React.Component<Props, State> {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    popUpListRef: RefObject<any>

    constructor(props: Props) {
        super(props)
        this.state = { direction: Direction.down }
        this.popUpListRef = createRef()
    }

    componentDidUpdate(prevProps: Props) {
        const { isExpanded } = this.props

        if (isExpanded && prevProps.isExpanded !== isExpanded) { this.updateDirection() }
    }

    updateDirection = () => {
        const { inputSelect } = this.props

        if (!inputSelect) { return null }

        const documentHeight = window.innerHeight ||
                document.documentElement.clientHeight ||
                document.body.clientHeight

        if (documentHeight - inputSelect.getBoundingClientRect().bottom < this.popUpListRef.current.offsetHeight) {
            this.setState({ direction: Direction.up })

            return null
        }

        this.setState({ direction: Direction.down })

        return null
    }

    render() {
        const { isExpanded, children, expandPopUp, inputSelect } = this.props
        const { direction } = this.state

        const inputSelectHeight = inputSelect ? inputSelect.offsetHeight : null
        const style = inputSelectHeight && direction === 'up'
            ? { marginBottom: `${inputSelectHeight as number + 2}px` }
            : {}

        return (
            <Dropdown isOpen={isExpanded} direction={direction}>
                <DropdownMenu
                    className={classNames(
                        'dropdown-menu',
                        'n2o-select-pop-up',
                        {
                            'd-block': isExpanded,
                            'd-none': !isExpanded,
                            expandPopUp,
                        },
                    )}
                    style={style}
                    flip
                >
                    <div ref={this.popUpListRef}>{children}</div>
                </DropdownMenu>
            </Dropdown>
        )
    }
}
