#!/usr/bin/env groovy

/*
 * Copyright (C) 2015
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@Grab('org.yaml:snakeyaml:1.15')

import org.yaml.snakeyaml.Yaml

def cli = new CliBuilder(usage: 'docker2plant.groovy [options] [docker_compose_file]')
//Can't use Option.UNLIMITED_VALUES with a comma separator since the docker_compose_file argument is consumed by this option
cli.i(longOpt: 'ignore', args: 1, argName: 'container',
      'To specify a container to be ignored. To sepcify multiple containers just specify the option multiple times')
cli.h(longOpt: 'help', 'Displays this help')
def options = cli.parse(args)

if (options.h) {
  cli.usage();
  return;
}
def ignoredContainers = options.is
def arguments = options.arguments()
def composeFile = arguments ? new File(arguments[0]) : new File('docker-compose.yml')
Yaml yaml = new Yaml()
def compose = yaml.load(new FileInputStream(composeFile))

println '''
@startuml

skinparam monochrome true
'''
compose.forEach { containerName, container ->
  container.links?.forEach {
    def (linkName, linkAlias) = it.tokenize(':')
    if (!(linkName in ignoredContainers)) {
      def aliasRepresentaion = linkAlias ? "\"$linkAlias\" " : ''
      println "[$containerName] --> $aliasRepresentaion[$linkName]"
    }
  }
  container.volumes_from?.findAll { !(it in ignoredContainers) }?.forEach {
    println "[$containerName] ..> [$it]"
  }
}
println '''
legend left
- Dotted arrows represent dependencies through volumes
endlegend

@enduml
'''
