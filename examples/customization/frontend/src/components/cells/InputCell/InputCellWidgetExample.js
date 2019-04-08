import React from 'react';
import Table from 'n2o/lib/components/widgets/Table/Table';
import TextTableHeader from 'n2o/lib/components/widgets/Table/headers/TextTableHeader';
import InputCell from './InputCell';

class InputCellWidgetExample extends React.Component {
    render() {
        const tableProps = {
            headers: [
                {
                    id: 'id_1',
                    component: TextTableHeader,
                    label: 'Пример InputCell'
                }
            ],
            cells: [
                {
                    id: 'id_1',
                    component: InputCell
                }
            ],
            datasource: [
                {
                    "id_1": {
                        "value": "Тестовое значение",
                        "disabled": false,
                        "type": "text",
                        "autoFocus": true,
                        "style": {
                            border: "1px solid lightgray",
                            borderRadius: "5px",
                            paddingLeft: "5px"
                        }
                    }
                }
            ]
        };

        return (
            <div>
                <Table
                    {...this.props}
                    headers={tableProps.headers}
                    cells={tableProps.cells}
                    datasource={tableProps.datasource}
                />
            </div>
        );
    }
}

export default InputCellWidgetExample;