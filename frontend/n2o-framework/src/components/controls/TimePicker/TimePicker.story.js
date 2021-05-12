import React from 'react'
import omit from 'lodash/omit'
import { Row, Col } from 'reactstrap'
import withForm from 'N2oStorybook/decorators/withForm'
import { forceReRender, storiesOf } from '@storybook/react'
import { StateDecorator, Store } from '@sambego/storybook-state'

import TimePickerMeta from './Timepicker.meta.json'
import TimePicker from './TimePicker'

const store = new Store({
    valueDigit1: '',
})
const form = withForm({ src: 'TimePicker' })
const stories = storiesOf('Контролы/Таймпикер', module)

store.subscribe(forceReRender)

stories.addDecorator(StateDecorator(store))

stories
    .add('Таймпикер', () => (
        <div>
            <button
                type="button"
                className="btn btn-secondary"
                onClick={() => store.set({ valueDefault: '10:20:30' })}
            >
          Установить значение 10:20:30
            </button>
            <hr />
            <TimePicker
                value={store.get('valueDefault')}
                onChange={value => store.set({ valueDefault: value })}
            />
        </div>
    ))
    .add(
        'Метаданные',
        form(() => omit(TimePickerMeta, ['defaultValue'])),
    )
    .add('digit', () => (
        <>
            <Row>
                <Col md={4}>
                    <pre>
                        <code>
                "format": "digit",
                            {' '}
                            <br />
                "mode": ["hours", "minutes", "seconds"],
                            <br />
                "timeFormat": "HH:mm:ss",
                        </code>
                    </pre>
                    <TimePicker
                        format="digit"
                        mode={['hours', 'minutes', 'seconds']}
                        timeFormat="HH:mm:ss"
                        onChange={value => store.set({ valueDigit1: value })}
                    />
                    <mark>
value:
                        {store.get('valueDigit1')}
                    </mark>
                </Col>
                <Col md={4}>
                    <pre>
                        <code>
                "format": "digit",
                            {' '}
                            <br />
                "mode": ["minutes", "seconds"],
                            <br />
                "timeFormat": "HH:mm:ss",
                        </code>
                    </pre>
                    <TimePicker
                        format="digit"
                        mode={['minutes', 'seconds']}
                        timeFormat="HH:mm:ss"
                        onChange={value => store.set({ valueDigit2: value })}
                    />
                    <mark>
value:
                        {store.get('valueDigit2')}
                    </mark>
                </Col>
                <Col md={4}>
                    <pre>
                        <code>
                "format": "digit",
                            {' '}
                            <br />
                "mode": ["minutes"],
                            <br />
                "timeFormat": "HH:mm:ss",
                        </code>
                    </pre>
                    <TimePicker
                        format="digit"
                        mode={['minutes']}
                        timeFormat="HH:mm:ss"
                        onChange={value => store.set({ valueDigit3: value })}
                    />
                    <mark>
value:
                        {store.get('valueDigit3')}
                    </mark>
                </Col>
            </Row>
            <hr />
            <Row>
                <Col md={4}>
                    <pre>
                        <code>
                "format": "digit",
                            {' '}
                            <br />
                "mode": ["hours", "seconds"],
                            <br />
                "timeFormat": "HH:ss",
                        </code>
                    </pre>
                    <TimePicker
                        format="digit"
                        mode={['hours', 'seconds']}
                        timeFormat="HH:ss"
                        onChange={value => store.set({ valueDigit4: value })}
                    />
                    <mark>
value:
                        {store.get('valueDigit4')}
                    </mark>
                </Col>
                <Col md={4}>
                    <pre>
                        <code>
                "format": "digit",
                            {' '}
                            <br />
                "mode": ["seconds"],
                            <br />
                "timeFormat": "ss",
                        </code>
                    </pre>
                    <TimePicker
                        format="digit"
                        mode={['seconds']}
                        timeFormat="ss"
                        onChange={value => store.set({ valueDigit5: value })}
                    />
                    <mark>
value:
                        {store.get('valueDigit5')}
                    </mark>
                </Col>
                <Col md={4}>
                    <pre>
                        <code>
                "format": "digit",
                            {' '}
                            <br />
                "mode": ["minutes"],
                            <br />
                "timeFormat": "mm:ss",
                        </code>
                    </pre>
                    <TimePicker
                        format="digit"
                        mode={['minutes']}
                        timeFormat="mm:ss"
                        onChange={value => store.set({ valueDigit6: value })}
                    />
                    <mark>
value:
                        {store.get('valueDigit6')}
                    </mark>
                </Col>
            </Row>
        </>
    ))
    .add('symbols', () => (
        <>
            <Row>
                <Col md={4}>
                    <pre>
                        <code>
                "format": "symbols",
                            {' '}
                            <br />
                "mode": ["hours", "minutes", "seconds"],
                            <br />
                "timeFormat": "HH:mm:ss",
                        </code>
                    </pre>
                    <TimePicker
                        format="symbols"
                        mode={['hours', 'minutes', 'seconds']}
                        timeFormat="HH:mm:ss"
                        onChange={value => store.set({ valuesymbols1: value })}
                    />
                    <mark>
value:
                        {store.get('valuesymbols1')}
                    </mark>
                </Col>
                <Col md={4}>
                    <pre>
                        <code>
                "format": "symbols",
                            {' '}
                            <br />
                "mode": ["minutes", "seconds"],
                            <br />
                "timeFormat": "HH:mm:ss",
                        </code>
                    </pre>
                    <TimePicker
                        format="symbols"
                        mode={['minutes', 'seconds']}
                        timeFormat="HH:mm:ss"
                        onChange={value => store.set({ valuesymbols2: value })}
                    />
                    <mark>
value:
                        {store.get('valuesymbols2')}
                    </mark>
                </Col>
                <Col md={4}>
                    <pre>
                        <code>
                "format": "symbols",
                            {' '}
                            <br />
                "mode": ["minutes"],
                            <br />
                "timeFormat": "HH:mm:ss",
                        </code>
                    </pre>
                    <TimePicker
                        format="symbols"
                        mode={['minutes']}
                        timeFormat="HH:mm:ss"
                        onChange={value => store.set({ valuesymbols3: value })}
                    />
                    <mark>
value:
                        {store.get('valuesymbols3')}
                    </mark>
                </Col>
            </Row>
            <hr />
            <Row>
                <Col md={4}>
                    <pre>
                        <code>
                "format": "symbols",
                            {' '}
                            <br />
                "mode": ["hours", "seconds"],
                            <br />
                "timeFormat": "HH:ss",
                        </code>
                    </pre>
                    <TimePicker
                        format="symbols"
                        mode={['hours', 'seconds']}
                        timeFormat="HH:ss"
                        onChange={value => store.set({ valuesymbols4: value })}
                    />
                    <mark>
value:
                        {store.get('valuesymbols4')}
                    </mark>
                </Col>
                <Col md={4}>
                    <pre>
                        <code>
                "format": "symbols",
                            {' '}
                            <br />
                "mode": ["seconds"],
                            <br />
                "timeFormat": "ss",
                        </code>
                    </pre>
                    <TimePicker
                        format="symbols"
                        mode={['seconds']}
                        timeFormat="ss"
                        onChange={value => store.set({ valuesymbols5: value })}
                    />
                    <mark>
value:
                        {store.get('valuesymbols5')}
                    </mark>
                </Col>
                <Col md={4}>
                    <pre>
                        <code>
                "format": "symbols",
                            {' '}
                            <br />
                "mode": ["minutes"],
                            <br />
                "timeFormat": "mm:ss",
                        </code>
                    </pre>
                    <TimePicker
                        format="symbols"
                        mode={['minutes']}
                        timeFormat="mm:ss"
                        onChange={value => store.set({ valuesymbols6: value })}
                    />
                    <mark>
value:
                        {store.get('valuesymbols6')}
                    </mark>
                </Col>
            </Row>
        </>
    ))
    .add('noZero', () => <TimePicker noZero />)
    .add('locale', () => (
        <div>
            <code>ru</code>
            <TimePicker locale="ru" />
            <hr />
            <code>en</code>
            <TimePicker locale="en" />
        </div>
    ))
    .add('disabled', () => <TimePicker disabled />)
    .add('defaultValue', () => <TimePicker defaultValue="10:20:30" />)
