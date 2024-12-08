ET Framework 8.1 Code Generation Tool

## Features

- Supports generation of Entity, Component, System and Handler code
- Supports automatic implementation of common interfaces
- Supports Server, Client and Share namespaces
- Supports message handler code generation
- Provides user-friendly GUI configuration

## Important Notes

- Entity classes will not automatically add suffix
- Component classes will automatically add "Component" suffix
- System classes will automatically add "System" suffix
- Handler classes will automatically add "Handler" suffix
- Handler class names should follow X2Y_ZZZ format, otherwise generic types need to be filled manually

## Usage

1. Right-click on project folder
2. Select New -> ET Code
3. Configure options in the popup dialog
4. Click OK to generate code

For any issues, please submit an Issue 