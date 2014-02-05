<?xml version="1.0" encoding="MacRoman"?>

<!--
    Document   : wsmlconverter.xsl
    Created on : October 23, 2007, 1:38 PM
    Author     : ajenkins
    Description:
        Convert old WSML files to the current format
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="xml"/>

    <xsl:template match="node() | @*">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="@property[.='steps']">
        <xsl:attribute name="property">picks</xsl:attribute>
    </xsl:template>

    <xsl:template match="@class[.='com.jenkins.weavingsimulator.datatypes.Step']">
        <xsl:attribute name="class">com.jenkins.weavingsimulator.datatypes.WeftPick</xsl:attribute>
    </xsl:template>
    
    <xsl:template match="@property[.='threads']">
        <xsl:attribute name="property">ends</xsl:attribute>
    </xsl:template>
    
    <xsl:template match="@class[.='com.jenkins.weavingsimulator.datatypes.WarpThread']">
        <xsl:attribute name="class">com.jenkins.weavingsimulator.datatypes.WarpEnd</xsl:attribute>
    </xsl:template>
</xsl:stylesheet>
