import React from 'react';
import StandartField from 'n2o/lib/components/widgets/Form/fieldsets/BlankFieldset';

class BackgroundImageFieldset extends React.Component {
    render() {
        return (
            <div style={{
                padding: '40px'
            }}>
                <h1>Пример BackgroundImageFieldset</h1>
                <div style={{
                    background: 'url("https://upload.wikimedia.org/wikipedia/commons/thumb/a/a7/React-icon.svg/1200px-React-icon.svg.png") repeat',
                    backgroundSize: '40px'
                }}>
                    <StandartField
                        children={this.props.children}
                    />
                </div>
            </div>
        );
    }
}

export default BackgroundImageFieldset;