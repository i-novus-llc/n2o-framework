import React, { Fragment } from 'react'
import { storiesOf } from '@storybook/react'
import withForm from 'N2oStorybook/decorators/withForm'

import Factory from '../../../core/factory/Factory'

import Slider, { Slider as SliderComponent } from './Slider'
import meta from './Slider.meta'

const stories = storiesOf('Контролы/Ползунок', module)
const form = withForm({ src: 'Slider' })

stories.addParameters({
    info: {
        propTables: [SliderComponent],
        propTablesExclude: [Slider, Factory],
    },
})

stories
    .add('Компонент', () => {
        const props = {
            multiple: meta.multiple,
            showTooltip: meta.showTooltip,
            tooltipPlacement: meta.tooltipPlacement,
            step: meta.step,
            vertical: meta.showTooltip,
            disabled: meta.showTooltip,
            dots: meta.showTooltip,
            min: meta.min,
            max: meta.max,
            marks: {},
            pushable: meta.showTooltip,
        }

        return <Slider {...props} />
    })
    .add('Метаданные', form(() => meta))
    .add('Тултип', () => (
        <div>
            <h4>showTooltip=true</h4>
            <Slider showTooltip />
            <br />
            <h4>tooltipPlacement:</h4>
            <div>tooltipPlacement='top'</div>
            <Slider showTooltip tooltipPlacement="top" />
            <div>tooltipPlacement='bottom'</div>
            <Slider showTooltip tooltipPlacement="bottom" />
            <div>tooltipPlacement='left'</div>
            <Slider showTooltip tooltipPlacement="left" />
            <div>tooltipPlacement='right'</div>
            <Slider showTooltip tooltipPlacement="right" />
            <br />
            <h4>tooltipFormatter:</h4>
            <div>
          tooltipFormatter =
                {' '}
                <code>"&#36;&#123;this&#125;%"</code>
            </div>
            <Slider
                showTooltip
                tooltipPlacement="top"
                tooltipFormatter={'${this}%'}
            />
            <div>
          tooltipFormatter =
                {' '}
                <code>"t=&#36;&#123;this&#125;℃"</code>
            </div>
            <Slider
                showTooltip
                tooltipPlacement="top"
                tooltipFormatter={'t=${this}℃'}
            />
        </div>
    ))
    .add('Подписи на оси', () => (
        <>
            <h4>
          marks=
                <code>
                    {`{
            '0': '0℃',
            '100': '100℃'
          }`}
                </code>
            </h4>
            <Slider
                marks={{ 0: '0℃', 100: '100℃' }}
                max={100}
                min={0}
                step={10}
            />
        </>
    ))
    .add('Мульти режим', () => (
        <>
            <h4>multiple=true</h4>
            <Slider
                multiple
                min={0}
                max={100}
                defaultValue={[20, '50.5']}
                step="5.5"
                dots
            />
            <div>pushable=true</div>
            <Slider
                multiple
                min={0}
                max={100}
                defaultValue={[20, '50.5']}
                pushable
                step="5.5"
                dots
            />
            <div>value=[10, 50, 90]</div>
            <Slider
                multiple
                min={0}
                max={100}
                defaultValue={[10, 50, 90]}
                pushable
                step="5.5"
                dots
            />
        </>
    ))
    .add('Вертикальный режим', () => (
        <>
            <h4>vertical=true</h4>
            <div style={{ height: '400px' }}>
                <Slider
                    marks={{ 0: '0℃', 100: '100℃' }}
                    max={100}
                    min={0}
                    step={10}
                    vertical
                />
                <Slider
                    marks={{ 0: '0℃', 100: '100℃' }}
                    max={100}
                    min={0}
                    step={10}
                    dots
                    multiple
                    showTooltip
                    defaultValue={[20, 60]}
                    vertical
                />
            </div>
        </>
    ))
    .add('Кастомные стили', () => (
        <>
            <h4>Стиль трекера</h4>
            <div>
          trackStyle=
                <code>{'{ backgroundColor: \'blue\' }'}</code>
            </div>
            <Slider
                marks={{ 0: '0℃', 100: '100℃' }}
                trackStyle={{ backgroundColor: 'blue' }}
                max={100}
                min={0}
                step={10}
                defaultValue={50}
            />
            <br />
            <div>
          trackStyle=
                <code>{'[{ backgroundColor: \'red\', height: \'6px\' }]'}</code>
          multiple=
                <code>true</code>
            </div>
            <Slider
                marks={{
                    0: '0K',
                    100: '100K',
                    20: '20K',
                    40: '40K',
                    60: '60K',
                    80: '80K',
                }}
                trackStyle={[{ backgroundColor: 'red', height: '6px' }]}
                multiple
                defaultValue={[20, 60]}
                max={100}
                min={0}
                step={10}
            />
            <br />
            <div>
          railStyle=
                <code>{'{ backgroundColor: \'orange\' }'}</code>
            </div>
            <Slider
                railStyle={{ backgroundColor: 'blue' }}
                max={100}
                min={0}
                step={10}
                defaultValue={50}
            />
            <br />
            <h4>Стиль ползунка</h4>
            <div>
          handleStyle=
                <code>{'{ backgroundColor: \'blue\', border: \'2px solid red\' }'}</code>
            </div>
            <Slider
                marks={{ 0: '0℃', 100: '100℃' }}
                handleStyle={{ backgroundColor: 'blue', border: '2px solid red' }}
                max={100}
                min={0}
                step={10}
                defaultValue={50}
            />
            <br />
            <h4>Стиль меток</h4>
            <div>
          dotStyle=
                <code>{'{ backgroundColor: \'blue\', border: \'2px solid red\' }'}</code>
          dots=
                <code>true</code>
            </div>
            <Slider
                dots
                dotStyle={{ backgroundColor: 'blue', border: '2px solid red' }}
                max={100}
                min={0}
                step={10}
                defaultValue={50}
            />
            <br />
        </>
    ))
