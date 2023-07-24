import React, { KeyboardEvent } from 'react'
import { Input } from 'reactstrap'

/**
 * N2OSelectInput
 * @reactProps {function} onChange - callback при изменение инпута
 * @reactProps {string} placeholder - placeholder
 * @reactProps {function} onSearch - callback при нажатии на кнопку поиска
 * @reactProps {string} value - значение инпута
 */

type Props = {
    onChange(value: string): void,
    onSearch(): void,
    placeholder: string,
    value: string
}

export class N2OSelectInput extends React.Component<Props> {
    handleKeyDown(event: KeyboardEvent) {
        const { onSearch } = this.props

        if (event.key === 'Enter') {
            onSearch()
        }
    }

    render() {
        const { placeholder, onChange, value } = this.props

        return (
            <div className="N2O-select-dropdown-search">
                <Input
                    placeholder={placeholder}
                    type="text"
                    onChange={e => onChange(e.target.value)}
                    value={value}
                    onKeyDown={e => this.handleKeyDown(e)}
                />
            </div>
        )
    }
}
